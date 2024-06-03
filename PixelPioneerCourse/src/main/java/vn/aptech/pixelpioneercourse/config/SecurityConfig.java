package vn.aptech.pixelpioneercourse.config;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.aptech.pixelpioneercourse.repository.UserRepository;
import vn.aptech.pixelpioneercourse.service.CustomOAuth2User;
import vn.aptech.pixelpioneercourse.service.CustomOAuth2UserService;
import vn.aptech.pixelpioneercourse.service.UserService;
import vn.aptech.pixelpioneercourse.dto.CustomOauth2User;
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
    
    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/**")
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers(HttpMethod.GET, "/app/login", "/app/register").anonymous()
                        .requestMatchers(HttpMethod.POST, "/app/login", "/app/register").anonymous()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated() // Changed from permitAll to authenticated for security
                )
                .oauth2Login(oauth -> {
                    oauth.loginPage("/app/login");
                    oauth.userInfoEndpoint(o -> {
                        o.userService(customOAuth2UserService);
                    });
                    oauth.successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
                            System.out.println(authentication.getPrincipal());
                            DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
                            String email = oauthUser.getAttribute("email");
                            processOAuthPostLogin(email);
                            response.sendRedirect("/app/course");
                        }
                    });
                }) // Added missing semicolon here
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
    
    @Autowired
    private ModelMapper mapper;
    
    public void processOAuthPostLogin(String email){
        Optional<User> opUser = userRepository.findByEmail(email);
        if(opUser.isEmpty()) {
            User user = new User();
            user.setActiveStatus(true);
            user.setCreatedAt(LocalDate.now());
            user.setProvider(Provider.GOOGLE);
            user.setRole(mapper.map("ROLE_USER", Role.class));
            user.setEmail(email);
            userRepository.save(user);
        }
    }
}