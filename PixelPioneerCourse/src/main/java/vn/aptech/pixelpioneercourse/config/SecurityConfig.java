package vn.aptech.pixelpioneercourse.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;
import vn.aptech.pixelpioneercourse.repository.RoleRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;
import vn.aptech.pixelpioneercourse.service.CustomOAuth2UserService;
import vn.aptech.pixelpioneercourse.service.RoleService;
import vn.aptech.pixelpioneercourse.service.UserService;
import vn.aptech.pixelpioneercourse.entities.Provider;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.middle.AuthenticationMiddleware;
import vn.aptech.pixelpioneercourse.until.*;
import vn.aptech.pixelpioneercourse.controller.CustomAuthenticationSuccessHandler;
import vn.aptech.pixelpioneercourse.dto.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private UserDetailsService userDetailsService;
    private UserService uService;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AuthenticationMiddleware authenticationMiddleware;

    @Autowired
    private HttpSession session;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    public void globalConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.GET, "/app/register", "/app/login").anonymous()
                        .requestMatchers(HttpMethod.GET, "/app/course/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/app/login/checkLogin", "/app/register").anonymous()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> {
                    oauth.loginPage("/app/register")
                         .loginPage("/app/login");
                    oauth.userInfoEndpoint(o -> o.userService(customOAuth2UserService));
                    oauth.successHandler(customAuthenticationSuccessHandler());
                })
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/error"))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessHandler(new LogoutSuccessHandler() {
                            @Override
                            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
                                if (authentication != null && authentication.getPrincipal() instanceof DefaultOidcUser) {
                                    request.getSession().invalidate();
                                    for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                                        cookie.setMaxAge(0);
                                        cookie.setPath("/");
                                        response.addCookie(cookie);
                                    }
                                }
                                response.sendRedirect("https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/app/login");
                            }
                        })
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .build();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(this);
    }

    @Bean
    @Order(2)
    public SecurityFilterChain api(HttpSecurity http) throws Exception {
        PublicRoutes.PublicRoutesManager.publicRoutes()
                .add(HttpMethod.GET, "/api/accounts")
                .add(HttpMethod.POST, "/api/login", "/api/register")
                .injectOn(http);
        http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationMiddleware, UsernamePasswordAuthenticationFilter.class)
                .cors(configurer -> new CorsConfiguration().applyPermitDefaultValues());
        return http.build();
    }

    public String generateUsername(String fullName) {
        String[] names = fullName.trim().split("\\s+");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[names.length - 1] : "";

        LocalDateTime now = LocalDateTime.now();
        String day = now.format(DateTimeFormatter.ofPattern("dd"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));

        String username = lastName + "." + firstName.charAt(0) + "." + day + month;

        String date = now.format(DateTimeFormatter.ofPattern("dMyyyy"));
        String alternativeUsername = firstName + date;

        return username.toLowerCase();
    }

    @Autowired
    private RoleRepository roleRepository;
    
//    private String accountDeactivate(RedirectAttributes ra) {
//    	ra.addFlashAttribute("loginErrorCondition", true);
//    	ra.addFlashAttribute("loginError", "Your account has been deactivated, please contact us via contact information for further support.");
//    	return "redirect:/app/login";
//    }

    public boolean processOAuthPostLogin(String email, String fullname) {
        Optional<User> opUser = null;
        boolean createdYet = false;
        try {
            opUser = userRepository.findByEmail(email);
            if (opUser.isEmpty()) throw new NotFoundException("Cannot find account");
            session.setAttribute("user", opUser.get());
            session.setAttribute("userId", opUser.get().getId());
            session.setAttribute("isUser", true);
            session.setAttribute("isAdmin", null);
            session.setAttribute("isInstructor", null);
            return false;
        } catch (Exception e) {
            if (createdYet == false) {
                try {
                    UserCreateDto user = new UserCreateDto();
                    user.setFullName(fullname);
                    user.setUsername(generateUsername(fullname));
                    user.setPassword(passwordEncoder().encode("123456"));
                    user.setEmail(email);
                    User u = mapper.map(user, User.class);
                    u.setActiveStatus(true);
                    u.setCreatedAt(LocalDate.now());
                    u.setProvider(Provider.GOOGLE);
                    u.setPhone(null);
                    List<Role> listRole = roleRepository.findAll();
                    u.setRole(null); //incase there is no "ROLE_USER" in db;
                    for (Role role : listRole) {
                        if (role.getRoleName().equals("ROLE_USER")) u.setRole(role);
                    }
                    userRepository.save(u);
                    createdYet = true;
                    session.setAttribute("user", u);
                    session.setAttribute("userId", u.getId());
                    session.setAttribute("isUser", true);
                    session.setAttribute("isAdmin", null);
                    session.setAttribute("isInstructor", null);
                    return false;
                } catch (Exception e2) {
                	return true;
                }
            } else {
                session.setAttribute("user", opUser.get());
                session.setAttribute("userId", opUser.get().getId());
                session.setAttribute("isUser", true);
                session.setAttribute("isAdmin", null);
                session.setAttribute("isInstructor", null);
                return false;
            }
        }
    }
}
