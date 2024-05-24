package vn.aptech.pixelpioneercourse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.Authentication;
import vn.aptech.pixelpioneercourse.dto.UserInfoForJWT;
import vn.aptech.pixelpioneercourse.dto.UsernamePasswordAuthentication;
import vn.aptech.pixelpioneercourse.entities.RefreshToken;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.jwt.JWT;
import vn.aptech.pixelpioneercourse.repository.RefreshTokenRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UsernamePasswordAuthenticationService {
    @Value("jwt.secret")
    public String TOKEN_SECRET;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private JWT jwt;

    private PasswordEncoder encoder;

    public Authentication processLogin(UsernamePasswordAuthentication body){
        String email = body.getUsername();
        String password = body.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email or password is invalid!"));

        //so sanh password
        if(!encoder.matches(password, user.getPassword())){
            throw new UsernameNotFoundException("Email or password is invalid!");
        }

        var expiredAt = LocalDateTime.now().plusDays(1);
        var accessToken = jwt.encode(user.getId(), user.getAuthorities(),expiredAt, TOKEN_SECRET);

        //Tao refreshToken
        refreshTokenRepository.disableRefreshTokenFromUser(user.getId());
        RefreshToken refreshToken = new RefreshToken(user, 7);
        refreshTokenRepository.save(refreshToken);

        Authentication authentication = new Authentication(new UserInfoForJWT(user), accessToken, refreshToken.getCode(), expiredAt);

        return authentication;
    }
}