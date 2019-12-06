package be.technocite.kafkavelib;

import be.technocite.kafkavelib.kafka.consumer.Consumer;
import be.technocite.kafkavelib.kafka.producer.Producer;
import be.technocite.kafkavelib.resource.VelibStationResource;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@SpringBootApplication
public class KafkaVelibApplication implements CommandLineRunner {

	private final String BROKER_IP = "localhost:9092";
	private final String TOPIC = "velib-stations";
	private final String GROUP_ID = "velib-group";
	private Collection<Thread> childrenThreads = newArrayList();

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KafkaVelibApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run();
	}

	public void run(String... args) throws InterruptedException {
		addProducer(BROKER_IP, TOPIC);
		addConsumer(BROKER_IP, TOPIC, GROUP_ID);
		addConsumer(BROKER_IP, TOPIC, GROUP_ID);
		for(Thread thread : childrenThreads) {
			thread.start();
		}
	}

	void addProducer(String brokerIp, String topic) throws InterruptedException {
		Thread producerT = new Thread(() -> new Producer(brokerIp, topic, new VelibStationResource()).run());
		childrenThreads.add(producerT);
		producerT.join();
	}

	void addConsumer(String brokerIp, String topic, String group) throws InterruptedException {
		Thread consumerT = new Thread(() -> new Consumer(brokerIp, topic, group).run());
		childrenThreads.add(consumerT);
		consumerT.join();
	}
}
