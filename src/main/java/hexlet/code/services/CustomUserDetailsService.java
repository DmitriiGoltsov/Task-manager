package hexlet.code.services;

import hexlet.code.models.User;
import hexlet.code.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hexlet.code.config.security.WebSecurityConfig.DEFAULT_AUTHORITIES;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(username)
                .map(this::buildSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with username :" + username + " not found"));
    }

    private UserDetails buildSpringUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                DEFAULT_AUTHORITIES
        );
    }
}
