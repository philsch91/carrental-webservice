package at.fhcampuswien.sde.carrentalwebservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;
import at.fhcampuswien.sde.carrentalwebservice.model.User;

@Configuration
public class UserDataBean {
    private static final Logger log = LoggerFactory.getLogger(UserDataBean.class);

    @Bean
    public CommandLineRunner initUser(UserRepository repository) {
        return (args) -> {
            User superuser = new User(1L,"admin");
            superuser.setPassword("admin");
            //log.info(superuser.toString());
            repository.saveAndFlush(superuser);

            //repository.saveAndFlush(new User(10L,"admin","admin"));

            User user = repository.findById(1L);
            log.info("User found with findById(1L):");
            log.info("--------------------------------");
            log.info(user.toString());
            log.info("");
        };
    }
}
