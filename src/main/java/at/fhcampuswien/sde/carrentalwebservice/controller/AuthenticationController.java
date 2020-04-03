package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;
import at.fhcampuswien.sde.carrentalwebservice.exception.AuthenticationForbiddenException;
import at.fhcampuswien.sde.carrentalwebservice.model.User;

import at.fhcampuswien.sde.carrentalwebservice.model.response.JwtTokenResponse;
import at.fhcampuswien.sde.carrentalwebservice.security.JwtAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserRepository repository;
    private JwtAuthenticationService authenticationService;

    public AuthenticationController(UserRepository repository, JwtAuthenticationService authenticationService){
        this.repository = repository;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/auth")
    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtTokenResponse> authenticateUser(@RequestBody User user){
        log.info("User authentication: " + user.toString());

        Optional<User> optUser = this.repository.findOneByEmail(user.getEmail());
        optUser.orElseThrow(() -> new AuthenticationForbiddenException(user));

        User savedUser = optUser.get();
        log.info("User: " + savedUser.toString());

        return new ResponseEntity<>(authenticationService.generateJwtToken(savedUser.getEmail(), user.getPassword()), HttpStatus.OK);
    }

}
