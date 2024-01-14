package com.igorbavand.authenticationapi.domain.service.authentication;

import com.igorbavand.authenticationapi.api.dto.user.UserResponseDto;
import com.igorbavand.authenticationapi.domain.User;
import com.igorbavand.authenticationapi.domain.exception.exception.NotFoundException;
import com.igorbavand.authenticationapi.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByLogin(username);
    }

    public UserResponseDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            var user =  userService.findByLogin(((UserDetails) principal).getUsername());
            return new UserResponseDto(user.getId(), user.getUsername(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());
        } else {
            throw new NotFoundException("User not found.");
        }
    }
}

