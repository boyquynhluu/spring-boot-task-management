package com.taskmanagement.serviceimpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskmanagement.entities.Role;
import com.taskmanagement.entities.User;
import com.taskmanagement.enums.UserStatus;
import com.taskmanagement.repositories.RoleRepository;
import com.taskmanagement.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Custom UserDetails")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        try {
            User user;
            if (usernameOrEmail.contains("@")) {
                user = userRepository.findByEmail(usernameOrEmail).orElseThrow(
                        () -> new UsernameNotFoundException("User not found with email: " + usernameOrEmail));
            } else {
                user = userRepository.findByUsername(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("User not exist by Username or Email"));
            }

            // Check Status
            String status = user.getStatus().name();
            if(status.equals(UserStatus.INACTIVE.name())) {
                throw new DisabledException("Tài khoản chưa được kích hoạt!");
            }
            if(status.equals(UserStatus.BANNED.name())) {
                throw new DisabledException("Tài Khoản Đã Bị Khóa!");
            }

            // Get Role
            Role role = roleRepository.findRoleById(user.getRoleId());
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.getName()));

            return new org.springframework.security.core.userdetails.User(
                    usernameOrEmail,
                    user.getPassword(),
                    authorities);
        } catch (Exception e) {
            log.error("Find By username or email error: ", e);
            throw new UsernameNotFoundException("Login failed!");
        }
    }
}
