package be.technocite.kafkavelib;

import be.technocite.kafkavelib.kafka.consumer.Consumer;
import be.technocite.kafkavelib.kafka.producer.Producer;
import be.technocite.kafkavelib.resource.VelibStationResource;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaVelibApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KafkaVelibApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run();
	}

	public void run(String... args) throws InterruptedException {
		Thread producerT = new Thread(() -> new Producer(new VelibStationResource()).run());
		Thread consumerT = new Thread(() -> new Consumer().run());
		producerT.join();
		consumerT.join();
		producerT.start();
		consumerT.start();
	}
}
