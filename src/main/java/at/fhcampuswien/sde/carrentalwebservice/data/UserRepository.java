package at.fhcampuswien.sde.carrentalwebservice.data;

import at.fhcampuswien.sde.carrentalwebservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByName(String name);
    User findById(long id);
}
