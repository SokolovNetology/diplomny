package repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloud_service.model.entities.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String userName);
}
