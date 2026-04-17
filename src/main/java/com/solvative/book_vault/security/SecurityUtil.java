package com.solvative.book_vault.security;


import com.solvative.book_vault.models.response.auth.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static boolean isMember(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MEMBER"));
    }

    public static Long extractMemberId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            return null;
        }
        return user.getMemberId();
    }
}
