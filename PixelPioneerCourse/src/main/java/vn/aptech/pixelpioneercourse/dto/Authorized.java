package vn.aptech.pixelpioneercourse.dto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import java.util.List;
import java.util.Optional;

@Getter
public class Authorized extends User {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;

    //dang nhap bang app
    public Authorized(vn.aptech.pixelpioneercourse.entities.User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getGrantedAuthorities());
    }

    //dang nhap = id
    public Authorized(int id, List<SimpleGrantedAuthority> authorities){
        super("USERNAME", "PASSWORD", authorities);
        this.id = id;
        this.name = "";
    }

    public Boolean isAdmin(){
        return getAuthorities()
                .stream()
                .anyMatch(r->r.getAuthority().equals("ROLE_ADMIN"));
    }

    public Boolean isInstructor(){
        return getAuthorities()
                .stream()
                .anyMatch(r->r.getAuthority().equals("ROLE_INSTRUCTOR"));
    }

    public String meOrAdminOrInstructor(int id) {
        Boolean admin = isAdmin();
        Boolean instructor = isInstructor();
        Boolean itsMe = getId() == id;
        if (admin) {
            return "ADMIN";
        } else if (instructor) {
            return "INSTRUCTOR";
        }
        return "STUDENT";
    }

    public static Optional<Authorized> current(){
        return Optional.ofNullable(SecurityContextHolder
                .getContext()
                .getAuthentication()
        ).map(org.springframework.security.core.Authentication::getPrincipal).map(p -> {
            if (p instanceof Authorized authorized){
                return authorized;
            }
            return null;
        });
    }

    public UsernamePasswordAuthenticationToken getAuthentication(){
        return new UsernamePasswordAuthenticationToken(this, null, getAuthorities());
    }

}
