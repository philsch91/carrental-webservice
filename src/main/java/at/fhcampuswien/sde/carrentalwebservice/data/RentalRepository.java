package at.fhcampuswien.sde.carrentalwebservice.data;

import at.fhcampuswien.sde.carrentalwebservice.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Rental findById(long id);
    //List<Rental> findByUser(User user);
}
