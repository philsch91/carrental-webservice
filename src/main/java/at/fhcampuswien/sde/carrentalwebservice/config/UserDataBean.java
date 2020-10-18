package at.fhcampuswien.sde.carrentalwebservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fhcampuswien.sde.carrentalwebservice.data.UserRepository;
import at.fhcampuswien.sde.carrentalwebservice.model.User;

import java.util.Optional;

@Configuration
public class UserDataBean {
    private static final Logger log = LoggerFactory.getLogger(UserDataBean.class);

    @Bean
    public CommandLineRunner initUser(UserRepository repository) {
        return (args) -> {
            /*
            long id = 1L;

            User superuser = this.repository.findById(id);

            if (superuser != null) {
                log.info("superuser found: " + superuser.toString());
                return;
            }
            superuser = new User(id, "admin@service.com");
            superuser.setPassword("admin");
            //log.info(superuser.toString());
            repository.saveAndFlush(superuser);
            */

            //repository.saveAndFlush(new User(10L,"admin","admin"));

            /*
            User user = repository.findById(1L);
            log.info("User found with findById(1L):");
            log.info("--------------------------------");
            log.info(user.toString());
            log.info("");

            Optional<User> user2 = repository.findOneByEmail("admin@service.com");
            log.info("User found with findByEmail('admin'):");
            log.info("--------------------------------");
            log.info(user2.toString());
            log.info("");
            */
        };
    }
}
