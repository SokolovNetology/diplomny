package service;

import lombok.RequiredArgsConstructor;
import model.entities.Role;
import org.springframework.stereotype.Service;
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
