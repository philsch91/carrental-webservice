package at.fhcampuswien.sde.carrentalwebservice.controller;

import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;
import at.fhcampuswien.sde.carrentalwebservice.model.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
public class AuthenticationController {

    private final UserRepository repository;

    public AuthenticationController(UserRepository repository){
        this.repository = repository;
    }

    @GetMapping("/auth")
    public List<User> getAllUsers(){
        return this.repository.findAll();
    }
}
