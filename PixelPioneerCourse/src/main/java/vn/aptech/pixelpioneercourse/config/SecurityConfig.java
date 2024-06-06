package vn.aptech.pixelpioneercourse.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
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
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.ui.Model;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.aptech.pixelpioneercourse.repository.RoleRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;
import vn.aptech.pixelpioneercourse.service.CustomOAuth2User;
import vn.aptech.pixelpioneercourse.service.CustomOAuth2UserService;
import vn.aptech.pixelpioneercourse.service.RoleService;
import vn.aptech.pixelpioneercourse.service.UserService;
import vn.aptech.pixelpioneercourse.Provider;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.middle.AuthenticationMiddleware;
import vn.aptech.pixelpioneercourse.until.*;
import vn.aptech.pixelpioneercourse.dto.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RoleService rService;


//    @Autowired
//    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }

//	@Override
//	@Order(1)
//	public void configure(HttpSecurity http) throws Exception {
//		http.csrf(AbstractHttpConfigurer::disable);
//    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private AuthenticationMiddleware authenticationMiddleware;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void globalConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    private HttpServletRequest requests;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.GET, "/app/register", "/app/login").anonymous()
                        .requestMatchers(HttpMethod.GET, "/app/course").permitAll() // Allows anonymous, USER, and INSTRUCTOR roles
                        .requestMatchers(HttpMethod.GET, "/app/login/checkLogin", "/app/register").anonymous()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> {
                    oauth.loginPage("/app/login");
                    oauth.userInfoEndpoint(o -> {
                        o.userService(customOAuth2UserService);
                    });
                    oauth.successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
                            System.out.println(oauthUser);
                            processOAuthPostLogin(oauthUser.getAttribute("email"), oauthUser.getAttribute("given_name"));
                            response.sendRedirect("/app/course");
                        }
                    });
                })
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // Updated to match '/logout' and method GET
                        .logoutSuccessUrl("/app/login")
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .build();
    }


    @Bean
    @Order(2) //thu tu chay
    public SecurityFilterChain api(HttpSecurity http) throws Exception{
        PublicRoutes.PublicRoutesManager.publicRoutes()
                .add(HttpMethod.GET, "/api/accounts")
                .add(HttpMethod.POST, "/api/login", "/api/register")
                .injectOn(http);
        http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(request-> request.anyRequest().authenticated())
                .sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationMiddleware, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(hdl->hdl.authenticationEntryPoint(
//                        (req, res, ex) -> ResponseEntity.status(403).build()))
                .cors(configurer-> new CorsConfiguration().applyPermitDefaultValues());
        return http.build();
    }

    public String generateUsername(String fullName) {
        // Split the full name into first and last names
        String[] names = fullName.trim().split("\\s+");
        String firstName = names[0];
        String lastName = names.length > 1 ? names[names.length - 1] : "";

        // Get the current year
        LocalDateTime now = LocalDateTime.now();
        String day = now.format(DateTimeFormatter.ofPattern("dd"));
        String month = now.format(DateTimeFormatter.ofPattern("MM"));

        // Generate username by combining last name, first initial, and year
        String username = lastName + "." + firstName.charAt(0) + "." + day + month;

        // Alternatively, generate username by combining first name and current date in dMyyyy format
        String date = now.format(DateTimeFormatter.ofPattern("dMyyyy"));
        String alternativeUsername = firstName + date;

        // Return the preferred username format
        return username.toLowerCase(); // or return alternativeUsername.toLowerCase();
    }

    @Autowired
    @Lazy
    private UserService userService;

    public String generateUniquePhoneNumber() {
        Random random = new Random();
        String phoneNumber;
        do {
            // Generate a 10-digit phone number starting with '0'
            phoneNumber = "0" + random.ints(9, 0, 10) //10 digit, ranges from 0 to 9
                    .mapToObj(Integer::toString)
                    .reduce("", (a, b) -> a + b);
        } while (!userService.checkPhone(phoneNumber)); // Check if the phone number already exists
        return phoneNumber;
    }

    @Autowired
    private RoleRepository roleRepository;

    public void processOAuthPostLogin(String email, String fullname){
        Optional<User> opUser = userRepository.findByEmail(email);
        Role student = roleRepository.findByRoleName("ROLE_USER").get();
        if(opUser.isEmpty()) {
            UserCreateDtoV2 user = new UserCreateDtoV2();
            user.setRole(student);
            user.setActiveStatus(true);
            user.setCreatedAt(LocalDate.now());
            user.setProvider(Provider.GOOGLE);
            user.setFullName(fullname);
            user.setUsername(generateUsername(fullname));
            PasswordEncoder ed = passwordEncoder();
            user.setPassword(ed.encode("1"));
            user.setPhone(generateUniquePhoneNumber());
            user.setEmail(email);
            userRepository.save(mapper.map(user, User.class));
        }
    }
}