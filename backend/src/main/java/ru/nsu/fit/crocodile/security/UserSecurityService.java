package ru.nsu.fit.crocodile.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.model.Role;
import ru.nsu.fit.crocodile.model.Status;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.repository.RoleRepository;
import ru.nsu.fit.crocodile.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("user_security_service")
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserData user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .disabled(user.getStatus() == Status.NOT_ENABLED)
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(UserData user) {
        List<Role> roles = roleRepository.findAllByUsersContains(user);
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().toUpperCase()))
                .collect(Collectors.toList());
    }
}
