package be.technocite.kafkavelib.kafka.consumer;

import be.technocite.kafkavelib.serialisation.KafkaJsonDeserializer;
import be.technocite.kafkavelib.model.Station;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

public class Consumer {

    private final Logger LOGGER = LoggerFactory.getLogger(Consumer.class.getName());
    private final KafkaConsumer<String, Station> KAFKA_CONSUMER;
    private final String BROKERS_IP;
    private final String GROUP_ID;
    private final String TOPIC;
    private Collection<City> cities = newArrayList();

    public Consumer(List<String> brokersIp, String topic, String groupId) {
        this.BROKERS_IP = String.join(",", brokersIp);
        this.GROUP_ID = groupId;
        this.TOPIC = topic;
        Properties props = consumerProps();
        KAFKA_CONSUMER = new KafkaConsumer<>(props);
        KAFKA_CONSUMER.subscribe(Collections.singletonList(TOPIC));
    }

    public void run() {
        try {
            // le faire tourner dans une boucle infinie
            //for (int i = 0; i < 100; i++)
            while(true)
            {
                ConsumerRecords<String, Station> records = KAFKA_CONSUMER.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, Station> record : records) {
                   //LOGGER.info("Key: " + record.key() + ", Value: " + record.value());
                    LOGGER.debug("Partition: " + record.partition() + ", Offset: " + record.offset());
                    processStationData(record.value());
                }

//                KAFKA_CONSUMER.commitAsync();
            }
        } catch (WakeupException e) {
            LOGGER.info("Received shutdown signal!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            KAFKA_CONSUMER.close();
        }
    }

    private Properties consumerProps() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS_IP);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "1");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class.getName());
        //properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return properties;
    }

    private void processStationData(Station value) throws IOException {
        String cityName = value.getContractName();
        Integer stationNumber = value.getNumber();
        String stationAddress = value.getAddress();
        Integer availableBikeStands = value.getAvailableBikeStands();

        Optional<City> city = cities.stream().filter(c -> c.name.equals(cityName)).findFirst();
        if(!city.isPresent()){
            city = Optional.of(new City(cityName));
            cities.add(city.get());
        }
        Optional<Station> station = city.get().stations.stream().filter(s -> s.getNumber().equals(stationNumber)).findFirst();
        if(!station.isPresent()) {
            station = Optional.of(new Station(stationNumber, availableBikeStands));
            city.get().stations.add(station.get());
        }
        int countDiff = availableBikeStands - station.get().getAvailableBikeStands();
        if(countDiff != 0) {
            station.get().setAvailableBikeStands(availableBikeStands);
            // le moins s'affichera tout seul car 10 - 12 = -2;
            LOGGER.info((countDiff > 0 ? "+" : "" ) + countDiff + " " + stationAddress + " (" + cityName + ")");
        }
    }

    private final class City {
        private String name;
        private Collection<Station> stations = new ArrayList<>();

        public City(String name) {
            this.name = name;
        }
    }
}