package at.fhcampuswien.sde.carrentalwebservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class WebserviceApplication {

	@Bean
	public PasswordEncoder getPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	private static final Logger log = LoggerFactory.getLogger(WebserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}
}
