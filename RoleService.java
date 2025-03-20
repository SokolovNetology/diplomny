package service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import model.entities.Role;
import repository.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Optional<Role> findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
