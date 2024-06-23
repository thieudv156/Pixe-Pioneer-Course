package vn.aptech.pixelpioneercourse.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import vn.aptech.pixelpioneercourse.config.SecurityConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final SecurityConfig config;

    public CustomAuthenticationSuccessHandler(SecurityConfig config) {
        this.config = config;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();

        String targetUrl = determineTargetUrl(request, oauthUser);

        if (config.processOAuthPostLogin(oauthUser.getAttribute("email"), oauthUser.getAttribute("given_name"))) {
            response.sendRedirect(targetUrl);
        } else {
            response.sendRedirect("/");
        }
    }

    private String determineTargetUrl(HttpServletRequest request, DefaultOidcUser oauthUser) {
        String targetUrl = "/app/login"; // default target URL

        if (request.getRequestURI().contains("/app/register")) {
            targetUrl = "/app/register";
        }

        return targetUrl;
    }
}
