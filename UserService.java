package service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repository.UserRepository;


import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public Optional<model.entities.User> findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Проверяем что User с таким именем есть в БД
        model.entities.User user = findUserByUserName(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User |'%s'| is not found", username)
        ));
        //Возвращаем сформированный стандартный UserDetail на основе нашего User
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

// На будущее
//    public void createNewUser(User user, String role) {
//        if (
//                findUserByUserName(user.getUserName()).isPresent() ||
//                        roleService.findRoleByName(role).isEmpty()
//        ) {
//            throw new InputDataException("User |'%s'| already exists");
//        }
//        user.setRoles(List.of(roleService.findRoleByName(role).get()));
//        userRepository.save(user);
//    }
}
