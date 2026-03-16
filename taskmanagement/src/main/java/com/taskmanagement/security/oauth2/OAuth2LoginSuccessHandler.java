package com.taskmanagement.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.taskmanagement.dto.AuthResponse;
import com.taskmanagement.entities.User;
import com.taskmanagement.repositories.UserRepository;
import com.taskmanagement.security.jwt.JwtTokenProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        String provider = token.getAuthorizedClientRegistrationId().toUpperCase();

        OAuth2User oauthUser = token.getPrincipal();

        String providerId = oauthUser.getAttribute("sub");
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        User user = userRepository.findByProviderAndProviderId(provider, providerId).orElseGet(() -> {
            User u = new User();
            u.setProvider(provider);
            u.setProviderId(providerId);
            u.setEmail(email);
            u.setName(name);
            return userRepository.save(u);
        });

        AuthResponse auth = jwtUtil.generateTokenOtherLocal(user);

        addCookie(response, "access_token", auth.getAccessToken(), 60 * 15);
        addCookie(response, "refresh_token", auth.getRefreshToken(), 60 * 60 * 24 * 30);

        response.sendRedirect("http://localhost:3000");
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {

        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }
}
