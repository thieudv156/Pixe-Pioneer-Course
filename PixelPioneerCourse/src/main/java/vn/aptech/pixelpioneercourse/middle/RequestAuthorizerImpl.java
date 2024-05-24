package vn.aptech.pixelpioneercourse.middle;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.aptech.pixelpioneercourse.jwt.JWT;
import vn.aptech.pixelpioneercourse.util.PublicRoutes;

import java.util.Objects;

@Component
//@Service
public class RequestAuthorizerImpl implements RequestAuthorizer{
    
    @Value("jwt.secret")
    private String JWT_SECRET;
    
    private JWT jwt;
    
    @Override
    public void tryAuthorizer(HttpServletRequest request, HttpServletResponse response) {
        if(PublicRoutes.PublicRoutesManager.publicRoutes().anyMatch(request)){
            return;
        }
        
        var token = Authorization.extract(request);
        if(Objects.isNull(token)){
            return;
        }
        
        try {
            var authorized = jwt.decode(token, JWT_SECRET); //giai ma token
            SecurityContextHolder.getContext().setAuthentication(authorized.getAuthentication());
        } catch (Exception e) {
            ResponseEntity.status(403).build();
        }
    }
}
