package vn.aptech.pixelpioneercourse.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Email or Password wrong!"));
//        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//        user.getRoles().forEach(role -> {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getShortName());
//            authorities.add(authority);
//        });
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(),
                enabled, 
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked, 
                user.getGrantedAuthorities()
        );
    }
    
    
    
}
