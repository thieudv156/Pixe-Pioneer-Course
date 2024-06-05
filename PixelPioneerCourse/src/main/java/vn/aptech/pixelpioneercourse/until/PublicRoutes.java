package vn.aptech.pixelpioneercourse.until;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class PublicRoutes {
    public static class PublicRoutesManager {
        public static final PublicRoutes routes = new PublicRoutes();

        private PublicRoutesManager() {
        }

        public static PublicRoutes publicRoutes() {
            return PublicRoutesManager.routes;
        }
    }

    private final Map<HttpMethod, String[]> routes = new HashMap<>();
    private final List<AntPathRequestMatcher> matchers = new ArrayList<>();

    public PublicRoutes add(HttpMethod method, String... routes) {
        this.routes.put(method, routes);
        Arrays.asList(routes).forEach(route -> this.matchers.add(new AntPathRequestMatcher(route, method.name())));
        return this;
    }

    public boolean anyMatch(HttpServletRequest request) {
        try {
            return this.matchers.stream().anyMatch(rm -> rm.matches(request));
        } catch (Exception e) {
            log.error("error on route matching.", e);
            return false;
        }
    }

    public void injectOn(HttpSecurity http) {
        this.routes.forEach((method, routes) -> {
            try {
                http.securityMatcher("/api/**").authorizeHttpRequests(au -> au.requestMatchers(method, routes).permitAll());
            } catch (Exception e) {
                log.error("error on set public routes", e);
            }
        });
    }
}