package at.fhcampuswien.sde.carrentalwebservice.data;

import at.fhcampuswien.sde.carrentalwebservice.WebserviceApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(WebserviceApplication.class);

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Application started");
    }
}
