package org.digitalmind.buildingblocks.core.jpaauditor.aware;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public class SpringSecurityAuditorAwareImpl implements AuditorAware<String> {
    private static String SYSTEM_USER = "system";
    private static String UNKNOWN_USER = "unknown";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(getUsername());
    }

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return SYSTEM_USER;
        }
        Object principal = authentication.getPrincipal();

        if (principal != null) {

            if (principal instanceof String) {
                return (String) principal;
            } else if (principal instanceof User) {
                return ((User) principal).getUsername();
            }
        }
        return UNKNOWN_USER;
    }
}
