package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonBackReference(value = "user-role")
    @JsonIgnoreProperties({"users", "id"})
    private Role role;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = true, unique = true)
    private String phone;

    @Column(nullable = false, name = "active_status")
    private boolean activeStatus = true;

    @Column(nullable = false, name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    @ToString.Exclude
    private List<Discussion> discussions;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"instructor", "category", "enrollments", "frontPageImage", "createdAt", "reviews", "price", "description", "isPublished","progresses","tests"})
    private List<Course> courses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Test> tests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    @ToString.Exclude
    private List<Progress> progresses;

    public List<String> getAuthorities() {
        if(role == null)
            return Collections.singletonList("ROLE_USER");
        return Collections.singletonList(role.getRoleName());
    }

    public SimpleGrantedAuthority getGrantedAuthorities() {
        return new SimpleGrantedAuthority(role.getRoleName());
    }

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private Provider provider;
}
