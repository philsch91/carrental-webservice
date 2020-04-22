package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;

import at.fhcampuswien.sde.carrentalwebservice.model.Currency;
import at.fhcampuswien.sde.carrentalwebservice.model.User;
import at.fhcampuswien.sde.carrentalwebservice.model.dto.UserInfo;
import at.fhcampuswien.sde.carrentalwebservice.model.response.GenericResponse;
import at.fhcampuswien.sde.carrentalwebservice.security.JwtAuthenticatedProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController extends BaseRestController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        //TODO: implement user roles
        return this.repository.findAll();
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserInfo() {
        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.repository.findOneByEmail(userEmail);

        if (!optUser.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();

        UserInfo userInfo = this.convertUserToUserInfo(user);

        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestBody UserInfo userInfo) {
        if (userInfo == null) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(), "Missing request body");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String userEmail = super.getAuthentication().getName();

        Optional<User> optUser = this.repository.findOneByEmail(userEmail);

        if (!optUser.isPresent()) {
            GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),"Invalid user");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = optUser.get();
        User userCopy = null;

        try {
            userCopy = (User) user.clone();
        } catch (CloneNotSupportedException ex) {
            log.info(ex.getMessage());
        }

        //change user settings

        if (userInfo.getDefaultCurrency() != null) {
            String currency = userInfo.getDefaultCurrency();
            int id = Currency.getCurrencyId(currency);

            if (id == -1) {
                GenericResponse response = new GenericResponse(HttpStatus.BAD_REQUEST.value(),currency + " is invalid");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Currency newDefaultCurrency = Currency.valueOf(currency);
            user.setDefaultCurrency(newDefaultCurrency);
        }

        //save user settings

        user = this.repository.saveAndFlush(user);

        //validate changes

        if (userCopy == null) {
            GenericResponse response = new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"User update failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userCopy.equals(user)) {
            GenericResponse response = new GenericResponse(HttpStatus.OK.value(),"User settings not changed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        GenericResponse response = new GenericResponse(HttpStatus.OK.value(),"User settings updated");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    protected UserInfo convertUserToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        Currency currency = user.getDefaultCurrency();
        userInfo.setDefaultCurrency(currency.name());

        return userInfo;
    }


}
