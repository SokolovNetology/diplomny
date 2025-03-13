package service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.cloud_service.model.entities.Role;
import ru.netology.cloud_service.repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
