package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.data.CarRepository;
import at.fhcampuswien.sde.carrentalwebservice.exception.CarNotFoundException;
import at.fhcampuswien.sde.carrentalwebservice.model.Car;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarController {
    private final CarRepository repository;

    public CarController(CarRepository repository){
        this.repository = repository;
    }

    @GetMapping("/cars")
    public List<Car> getAllCars(){
        return this.repository.findAll();
    }

    @GetMapping("/cars/{id}")
    public Car getCar(@PathVariable Long id){
        return this.repository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
    }

    @PostMapping("/cars")
    public Car saveCar(@RequestBody Car car){
        return this.repository.save(car);
    }

    @PutMapping("/cars/{id}")
    public Car updateCar(@RequestBody Car newCar, @PathVariable Long id){
        return this.repository.findById(id).map(car -> {
            car.setName(newCar.getName());
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
}
