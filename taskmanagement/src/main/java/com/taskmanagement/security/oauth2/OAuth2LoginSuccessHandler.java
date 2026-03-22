package com.taskmanagement.security.oauth2;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.taskmanagement.constants.Constants;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        String provider = token.getAuthorizedClientRegistrationId().toUpperCase();

        OAuth2User oauthUser = token.getPrincipal();

        String providerId = oauthUser.getAttribute("sub");
        String name = oauthUser.getAttribute("name");
        String emailOauth2 = oauthUser.getAttribute("email");

        // Check exist email
        User user = userRepository.getUserByEmail(emailOauth2);
        if(Objects.isNull(user)) {
            User u = new User();
            u.setId(userRepository.getMaxId() + 1);
            u.setName(name);
            u.setEmail(emailOauth2);
            u.setProvider(provider);
            u.setProviderId(providerId);
            u.setCreatedAt(LocalDateTime.now());
            u.setUpdatedAt(LocalDateTime.now());
            // save User
            userRepository.save(u);

            user = u;
        } else {
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setUpdatedAt(LocalDateTime.now());
            // save User
            userRepository.save(user);
        }

        AuthResponse auth = jwtTokenProvider.generateTokenOtherLocal(user);

        this.setTokenInCookie(response, auth.getAccessToken(), auth.getRefreshToken());

        response.sendRedirect("http://localhost:3000/home");
    }

    private void setTokenInCookie(HttpServletResponse response, String accessToken, String refreshToken) {
        // Access token: 15 phút
        ResponseCookie accessCookie = setCookie(Constants.ACCESS_TOKEN, accessToken, false, Duration.ofMinutes(15));
        // Refresh token: 30 ngày
        ResponseCookie refreshCookie = setCookie(Constants.REFRESH_TOKEN, refreshToken, true, Duration.ofDays(30));

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    private ResponseCookie setCookie(String name, String value, boolean httpOnly, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(httpOnly)
                .secure(false) // local dev, production nên để true
                .sameSite("None") // nếu FE và BE khác domain
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
