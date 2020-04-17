package at.fhcampuswien.sde.carrentalwebservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.Vector;

import at.fhcampuswien.sde.carrentalwebservice.data.CarFactory;
import at.fhcampuswien.sde.carrentalwebservice.data.CarRepository;
import at.fhcampuswien.sde.carrentalwebservice.data.RentalRepository;
import at.fhcampuswien.sde.carrentalwebservice.exception.CarNotFoundException;
import at.fhcampuswien.sde.carrentalwebservice.model.Car;
import at.fhcampuswien.sde.carrentalwebservice.model.Rental;
import at.fhcampuswien.sde.carrentalwebservice.model.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CarController {
    private final CarRepository repository;
    private final RentalRepository rentalRepository;

    public CarController(CarRepository repository, RentalRepository rentalRepository){
        this.repository = repository;
        this.rentalRepository = rentalRepository;
    }

    @GetMapping("/cars")
    public List<Car> getAllCars(){
        List<Car> carList = this.repository.findAll();

        List<Car> availableCarList = new Vector<Car>();

        for (Car car : carList) {
            boolean isCarAvailable = this.checkForCarBooking(car);

            if (isCarAvailable) {
                availableCarList.add(car);
            }
        }


        CarFactory carFactory = new CarFactory();
        availableCarList = carFactory.randomUpdateCarLocations(availableCarList);

        availableCarList = this.repository.saveAll(availableCarList);

        return availableCarList;
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity getCar(@PathVariable Long id){
        Optional<Car> optionalCar = this.repository.findById(id);

        if (!optionalCar.isPresent()) {
            throw new CarNotFoundException(id);
        }

        Car car = optionalCar.get();

        boolean isCarAvailable = this.checkForCarBooking(car);

        if (!isCarAvailable) {
            GenericResponse response = new GenericResponse(HttpStatus.OK.value(), "Car is not available");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @PostMapping("/cars")
    public Car saveCar(@RequestBody Car car){
        return this.repository.save(car);
    }

    @PutMapping("/cars/{id}")
    public Car updateCar(@RequestBody Car newCar, @PathVariable Long id){
        return this.repository.findById(id).map(car -> {
            car.setType(newCar.getType());
            return this.repository.save(car);
        }).orElseGet(() -> {
            newCar.setId(id);
            return repository.save(newCar);
        });
    }

    @DeleteMapping("/cars/{id}")
    public void deleteCar(@PathVariable Long id){
        this.repository.deleteById(id);
    }

    protected boolean checkForCarBooking(Car car) {
        List<Rental> rentalsForCar = this.rentalRepository.findByCar(car);
        boolean isCarAvailable = true;

        for (Rental rental : rentalsForCar) {
            if (rental.getEndDate() == null) {
                isCarAvailable = false;
            }
        }

        return isCarAvailable;
    }
}
