package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.cloud_service.model.entities.Role;


import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
