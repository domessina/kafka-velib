package be.technocite.kafkavelib.kafka.producer;

import be.technocite.kafkavelib.serialisation.KafkaJsonSerializer;
import be.technocite.kafkavelib.model.Station;
import be.technocite.kafkavelib.resource.VelibStationResource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class Producer {

    private final KafkaProducer<String, Station> KAFKA_PRODUCER;
    private final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private final String BROKER_IP;
    private final String TOPIC;
    private VelibStationResource velibStationResource;

    public Producer(List<String> brokersIp, String topic, VelibStationResource velibStationResource) {
        this.BROKER_IP = String.join(",", brokersIp);
        this.TOPIC = topic;
        this.velibStationResource = velibStationResource;
        Properties props = producerProps();
        KAFKA_PRODUCER = new KafkaProducer<>(props);
        LOGGER.info("be.technocite.Producer initialized");
    }

    private Properties producerProps() {
        // serialiszer une chaine de charactère pour la passer sur le réseau
        // ceci est un des serializer de kafka
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_IP);
        props.setProperty(ProducerConfig.METADATA_MAX_AGE_CONFIG, "1");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,  StringSerializer.class.getName());
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());

        return props;
    }

    public void run() {
        //for (int i = 0; i < 50; i++) {
        while(true) {
            try {
                fetchRecords();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                close();
            }
        }
        //close();
    }

    private void fetchRecords() {
        for(Station station : velibStationResource.getStationsData()) {
            // key = mot clef pour que kafka sétermine sur quelle partition écrire la value
            put(TOPIC, station.getNumber().toString(), station);
        }
    }

    void put(String topic, String key, Station value) {
        //LOGGER.info("Put value: " + value + ", for key: " + key);

        // on peut passer une clef pour influencer la partition choisie, utile généralement que si on fait du log compaction
        ProducerRecord<String, Station> record = new ProducerRecord<>(topic, key, value);
        //ProducerRecord<String, Station> record = new ProducerRecord<>(topic, value);
        KAFKA_PRODUCER.send(record, (recordMetadata, e) -> {
            if (e != null) {
                LOGGER.error("Error while producing", e);
                return;
            }

            LOGGER.debug("Received new meta. Topic: " + recordMetadata.topic()
                    + "; Partition: " + recordMetadata.partition()
                    + "; Offset: " + recordMetadata.offset()
                    + "; Timestamp: " + recordMetadata.timestamp());
        });
    }

    void close() {
        LOGGER.info("Closing producer's connection");
        // vide la file d'attente et force l'envoi
        KAFKA_PRODUCER.flush();
        KAFKA_PRODUCER.close();
    }
}