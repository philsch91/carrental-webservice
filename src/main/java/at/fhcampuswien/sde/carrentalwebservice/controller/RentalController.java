package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.data.CarFactory;
import at.fhcampuswien.sde.carrentalwebservice.data.CarRepository;
import at.fhcampuswien.sde.carrentalwebservice.data.RentalRepository;
import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;
import at.fhcampuswien.sde.carrentalwebservice.exception.AuthenticationForbiddenException;
import at.fhcampuswien.sde.carrentalwebservice.model.Car;
import at.fhcampuswien.sde.carrentalwebservice.model.Rental;
import at.fhcampuswien.sde.carrentalwebservice.model.User;
import at.fhcampuswien.sde.carrentalwebservice.model.dto.Booking;
import at.fhcampuswien.sde.carrentalwebservice.model.response.GenericResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

@RestController
public class RentalController extends BaseRestController {

    private static final Logger log = LoggerFactory.getLogger(RentalController.class);
    private final RentalRepository repository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public RentalController(RentalRepository repository, UserRepository userRepository, CarRepository carRepository){
        this.repository = repository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    @GetMapping("/rental")
    public List<Booking> getAllRentals(){
        /*
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof JwtAuthenticatedProfile)) {
            throw new AuthenticationForbiddenException("authentication failure");
        }

        log.info(auth.toString());
        log.info(Boolean.toString(auth.isAuthenticated()));
        log.info(auth.getName());
        log.info(auth.getPrincipal().toString());

        JwtAuthenticatedProfile authenticatedProfile = (JwtAuthenticatedProfile) auth;
        */

        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.userRepository.findOneByEmail(userEmail);

        if (!optUser.isPresent()){
            throw new AuthenticationForbiddenException("authentication failure");
        }

        User user = optUser.get();

        List<Rental> rentals = this.repository.findByUser(user);
        List<Booking> bookings = new Vector<Booking>();

        for(Rental rental : rentals) {
            bookings.add(this.convertRentalToBooking(rental));
        }

        return bookings;
    }

    @GetMapping("/rental/{id}")
    public ResponseEntity getRental(@PathVariable Long id){
        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.userRepository.findOneByEmail(userEmail);

        if (!optUser.isPresent()){
            throw new AuthenticationForbiddenException("authentication failure");
        }

        User user = optUser.get();

        List<Rental> userRentalList = this.repository.findByUser(user);
        Rental rental = null;

        for (Rental userRental : userRentalList) {
            if (userRental.getId() == id.longValue()) {
                rental = userRental;
            }
        }

        if (rental == null) {
            //throw new RentalNotFoundException(id);
            GenericResponse response = new GenericResponse(HttpStatus.NOT_FOUND.value(), "Rental not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Booking booking = this.convertRentalToBooking(rental);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @RequestMapping(value = "/rental", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveRental(@RequestBody Booking booking){
        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.userRepository.findOneByEmail(userEmail);

        if (!optUser.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();

        Long carId = booking.getCarId();

        if (carId == null) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"carId is missing in request payload");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<Car> optionalCar = this.carRepository.findById(carId);

        if (!optionalCar.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Car not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Car car = optionalCar.get();

        //safety check for car availability

        List<Rental> rentalsForCar = this.repository.findByCar(car);
        for (Rental rental : rentalsForCar) {
            if (rental.getEndDate() == null) {
                GenericResponse response = new GenericResponse(400,"Car already booked");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        long unixTimestamp = Instant.now().getEpochSecond();

        //validity check for booking.startTime

        if (booking.getStartTime() != null
                && booking.getEndTime() > unixTimestamp - 10
                && booking.getEndTime() < unixTimestamp + 10) {
            unixTimestamp = booking.getEndTime();
        }

        Timestamp startDate = new Timestamp(unixTimestamp * 1000);

        //save rental

        Rental rental = new Rental();
        rental.setStartDate(startDate);
        rental.setUser(user);
        rental.setCar(car);

        Rental savedRental = this.repository.save(rental);

        if (savedRental == null) {
            GenericResponse response = new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Car already booked");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //return booking

        //GenericResponse response = new GenericResponse(200, "Booking successful");
        Booking bookingResponse = this.convertRentalToBooking(savedRental);

        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    //@PutMapping("/rental/{id}")
    @RequestMapping(value = "/rental/{id}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRental(@RequestBody Booking booking, @PathVariable Long id){
        if (booking.getId() != null && !booking.getId().equals(id)) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(), "Incorrect rental id");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.userRepository.findOneByEmail(userEmail);

        if (!optUser.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();

        Optional<Rental> optionalRental = this.repository.findById(id);

        if (!optionalRental.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Rental not found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Rental rental = optionalRental.get();

        //compare user with rental user

        User rentalUser = rental.getUser();

        log.info("token user: " + user.getId());
        log.info("rental user: " + rentalUser.getId());

        if (user.getId() != rentalUser.getId()) {
            GenericResponse response = new GenericResponse(HttpStatus.UNAUTHORIZED.value(),"Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        //compare car id in the booking with car id for the saved rental

        if (booking.getCarId() != null && booking.getCarId().longValue() != rental.getCar().getId()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid car id");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        //check rental endDate

        if (rental.getEndDate() != null) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Rental already ended");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        long unixTimestamp = Instant.now().getEpochSecond();

        //validity check for booking.endTime

        if (booking.getEndTime() != null
            && booking.getEndTime() > unixTimestamp - 10
            && booking.getEndTime() < unixTimestamp + 10) {
            unixTimestamp = booking.getEndTime();
        }

        //set end date and price

        Timestamp endDate = new Timestamp(unixTimestamp * 1000);
        rental.setEndDate(endDate);

        rental = this.repository.save(rental);

        //update car location

        List<Car> exceptionCarList = this.carRepository.findAll();

        //CarController carController = new CarController(this.carRepository, this.repository);
        //List<Car> exceptionCarList = carController.getAllCars();

        CarFactory carFactory = new CarFactory();
        Car newCar = carFactory.buildNewRandomCar(exceptionCarList);

        Car rentalCar = rental.getCar();
        rentalCar.setLatitude(newCar.getLatitude());
        rentalCar.setLongitude(newCar.getLongitude());

        this.carRepository.save(rentalCar);

        //return booking

        //return new ResponseEntity<>(rental, HttpStatus.OK);
        Booking bookingResponse = this.convertRentalToBooking(rental);

        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @DeleteMapping("/rental/{id}")
    public ResponseEntity deleteRental(@PathVariable Long id) {
        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.userRepository.findOneByEmail(userEmail);

        if (!optUser.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();

        //TODO: implement user roles

        long userId = user.getId();
        String email = user.getEmail();

        if (userId != 1L && !email.equals("admin@service.com")) {
            GenericResponse response = new GenericResponse(HttpStatus.FORBIDDEN.value(),"Request forbidden");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        Optional<Rental> optionalRental = this.repository.findById(id);

        if (!optionalRental.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.NOT_FOUND.value(), "Rental " + id.toString() + " not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        this.repository.deleteById(id);

        GenericResponse response = new GenericResponse(HttpStatus.OK.value(),"Rental " + id.toString() + " deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected Booking convertRentalToBooking(Rental rental) {
        Booking booking = new Booking();
        booking.setId(rental.getId());
        booking.setCarId(rental.getCar().getId());

        if (rental.getStartDate() != null) {
            Timestamp startDate = rental.getStartDate();
            booking.setStartTime(startDate.getTime() / 1000);
        }

        if (rental.getEndDate() != null) {
            Timestamp endDate = rental.getEndDate();
            booking.setEndTime(endDate.getTime() / 1000);
        }

        booking.setPrice(rental.getPrice());

        return booking;
    }
}
