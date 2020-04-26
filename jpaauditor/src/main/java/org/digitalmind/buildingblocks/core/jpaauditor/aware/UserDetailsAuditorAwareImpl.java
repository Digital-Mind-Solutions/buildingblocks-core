package org.digitalmind.buildingblocks.core.jpaauditor.aware;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class UserDetailsAuditorAwareImpl implements AuditorAware<UserDetails> {
    private static UserDetails DEFAULT_USER = User.withUsername("unknown").build();

    @Override
    public Optional<UserDetails> getCurrentAuditor() {
        return Optional.of(getUsername());
    }

    public static UserDetails getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return DEFAULT_USER;
        }

        Object principal = authentication.getPrincipal();

        if (principal != null) {
            if (principal instanceof String) {
                return User.withUsername((String) principal).build();
            } else if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }
        return DEFAULT_USER;
    }
}
