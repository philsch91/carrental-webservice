package at.fhcampuswien.sde.carrentalwebservice.config;

import at.fhcampuswien.sde.carrentalwebservice.data.CarRepository;
import at.fhcampuswien.sde.carrentalwebservice.model.Car;
import at.fhcampuswien.sde.carrentalwebservice.model.CarType;
import at.fhcampuswien.sde.carrentalwebservice.data.CarFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

@Component
public class CarDataRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CarDataRunner.class);
    private final CarRepository repository;

    public CarDataRunner(CarRepository carRepository) {
        this.repository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //repository.save(new Car(1L,"Car1"));
        //repository.save(new Car(2L, "Car2"));
        //repository.save(new Car(3L, "Car3"));

        CarFactory carFactory = new CarFactory();
        List<Car> carList = carFactory.buildCars();

        int index = 0;
        long id = 1L;

        while (index < (carList.size()/4)*3) {
            Car car = carList.get(index);
            car.setId(id);
            this.repository.save(car);

            index++;
            id++;
        }

        log.info("Cars found with findAll():");
        log.info("-------------------------------");
        for (Car car : this.repository.findAll()) {
            log.info(car.toString());
        }
        log.info("");

        Car car = this.repository.findById(1L);
        log.info("Car found with findById(1L):");
        log.info("--------------------------------");
        log.info(car.toString());
        log.info("");

        log.info("Car found with findByType(CarType.SMALL):");
        log.info("--------------------------------------------");
        /*
		repository.findByType(CarType.SMALL).forEach(car -> {
			log.info(car.toString());
		}); */
        for (Car c : this.repository.findByType(CarType.SMALL)) {
            log.info(c.toString());
        }
        log.info("");
    }
}
