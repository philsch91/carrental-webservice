package at.fhcampuswien.sde.carrentalwebservice;

import at.fhcampuswien.sde.carrentalwebservice.data.CarRepository;
import at.fhcampuswien.sde.carrentalwebservice.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarrentalwebserviceApplication {

	private static final Logger log = LoggerFactory.getLogger(CarrentalwebserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarrentalwebserviceApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(CarRepository repository) {
		return (args) -> {
			repository.save(new Car(1L,"Car1"));
			repository.save(new Car(2L, "Car2"));
			repository.save(new Car(3L, "Car3"));

			log.info("Cars found with findAll():");
			log.info("-------------------------------");
			for (Car car : repository.findAll()) {
				log.info(car.toString());
			}
			log.info("");

			Car car = repository.findById(1L);
			log.info("Car found with findById(1L):");
			log.info("--------------------------------");
			log.info(car.toString());
			log.info("");

			log.info("Car found with findByLastName('Car2'):");
			log.info("--------------------------------------------");
			/*
			repository.findByName("Car2").forEach(car2 -> {
				log.info(car2.toString());
			}); */
			for (Car c : repository.findByName("Car2")) {
				log.info(c.toString());
			}
			log.info("");
		};
	}
}
