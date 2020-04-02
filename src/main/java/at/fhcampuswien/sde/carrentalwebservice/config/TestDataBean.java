package at.fhcampuswien.sde.carrentalwebservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fhcampuswien.sde.carrentalwebservice.data.CarRepository;
import at.fhcampuswien.sde.carrentalwebservice.model.Car;

@Configuration
public class TestDataBean {
    private static final Logger log = LoggerFactory.getLogger(TestDataBean.class);

    @Bean
    public CommandLineRunner initCars(CarRepository repository) {
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
