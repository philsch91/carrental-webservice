package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.data.RentalRepository;
import at.fhcampuswien.sde.carrentalwebservice.exception.RentalNotFoundException;
import at.fhcampuswien.sde.carrentalwebservice.model.Rental;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class RentalController {

    private static final Logger log = LoggerFactory.getLogger(RentalController.class);
    private final RentalRepository repository;

    public RentalController(RentalRepository repository){
        this.repository = repository;
    }

    @GetMapping("/rental")
    public List<Rental> getAllRentals(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.toString());
        log.info(Boolean.toString(auth.isAuthenticated()));
        log.info(auth.getName());
        if(auth.getPrincipal() != null){
            log.info(auth.getPrincipal().toString());
        }

        return this.repository.findAll();
    }

    @GetMapping("/rental/{id}")
    public Rental getRental(@PathVariable Long id){
        return this.repository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    @GetMapping("/rental/user/{id}")
    public List<Rental> getRentalForUser(@PathVariable Long id){
        /*
        return this.repository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
         */
        return null;
    }

    @PostMapping("/rental")
    public Rental createRental(@RequestBody Rental newRental){
        //TODO: check for user object in rental object
        return this.repository.save(newRental);
    }

    @PutMapping("/rental/{id}")
    public Rental updateRental(@RequestBody Rental rental, @PathVariable Long id){
        //TODO: check if rental is linked to user

        //GenericResponse response = new GenericResponse(409,"User already registered");
        //return new ResponseEntity<>(response,HttpStatus.CONFLICT);

        /*
        return this.repository.findById(id).map(car -> {
            car.setType(newCar.getType());
            return this.repository.save(car);
        }).orElseGet(() -> {
            newCar.setId(id);
            return repository.save(newCar);
        });
        */

        return null;
    }

    @DeleteMapping("/rental/{id}")
    public void deleteCar(@PathVariable Long id){
        //TODO: check for super user rights
        this.repository.deleteById(id);
    }
}
