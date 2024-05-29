package vn.aptech.pixelpioneercourse.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import vn.aptech.pixelpioneercourse.repository.UserRepository;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.middle.AuthenticationMiddleware;
import vn.aptech.pixelpioneercourse.until.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
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
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/loginAccess").permitAll()
                        .anyRequest().permitAll()
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
//                .addFilterBefore(authenticationMiddleware, UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling(hdl->hdl.authenticationEntryPoint(
//                        (req, res, ex) -> ResponseEntity.status(403).build()))
                .cors(configurer-> new CorsConfiguration().applyPermitDefaultValues());
        return http.build();
    }
    
    public void processOAuthPostLogin(String email){
        Optional<User> opUser = userRepository.findByEmail(email);
        if(opUser.isEmpty()) {
            User user = new User();
            user.setEmail(email);
            userRepository.save(user);
        }
    }
}