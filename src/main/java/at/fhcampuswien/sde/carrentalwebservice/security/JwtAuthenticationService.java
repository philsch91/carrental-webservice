package at.fhcampuswien.sde.carrentalwebservice.security;

import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;
import at.fhcampuswien.sde.carrentalwebservice.exception.AuthenticationForbiddenException;
import at.fhcampuswien.sde.carrentalwebservice.model.User;
import at.fhcampuswien.sde.carrentalwebservice.model.response.JwtTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationService.class);
    private UserRepository userRepository;
    private JwtTokenAuthorizationService tokenService;
    private PasswordEncoder passwordEncoder;

    public JwtAuthenticationService(UserRepository userRepository, JwtTokenAuthorizationService tokenService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateJwtToken(String email, String password){
        log.info("generateJwtToken");
        log.info(password);

        /*
        return this.userRepository.findOneByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> new JwtTokenResponse(tokenService.generateToken(email)))
                .orElseThrow(() -> new AuthenticationForbiddenException("incorrect password"));
         */

        Optional<User> optionalUser = this.userRepository.findOneByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new JwtAuthenticationException("incorrect password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new JwtAuthenticationException("incorrect password");
        }

        String token = tokenService.generateToken(email);

        return token;
    }
}
