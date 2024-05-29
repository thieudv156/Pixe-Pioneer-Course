package vn.aptech.pixelpioneercourse.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import vn.aptech.pixelpioneercourse.dto.Authorized;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JWTImpl implements JWT{

    private SecretKey secretToKey(String secret)
    {
        var bytes = secret.getBytes(Strings.UTF_8);
        try{
            return Keys.hmacShaKeyFor(bytes);
        } catch (WeakKeyException e){
            log.info("Creating jwt key with weak key");
            return Keys.hmacShaKeyFor(Arrays.copyOf(bytes,64));
        }
    }
    @Override
    public String encode(int id, List<String> roles, LocalDateTime expiredAt, String secret) {
        log.info("Creating new jwt, id: [{}], roles: [{}]", id, roles.toString());
        var accessToken = Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("roles", String.join(",", roles))
                .setExpiration(Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretToKey(secret))
                .compact();
        log.info("Successfully created new jwt: [{}]", accessToken);
        return accessToken;
    }

    @Override
    public Authorized decode(String token, String secret) {
        log.info("Decode jwt");
        Jws<Claims> decodedToken = Jwts.parserBuilder()
                .setSigningKey(secretToKey(secret))
                .build()
                .parseClaimsJws(token);

        String id = decodedToken
                .getBody()
                .getSubject();

        String rolesString = decodedToken
                .getBody()
                .get("roles", String.class); // Ensure you get the roles as a String

        List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new Authorized(Integer.parseInt(id), authorities);
    }
}