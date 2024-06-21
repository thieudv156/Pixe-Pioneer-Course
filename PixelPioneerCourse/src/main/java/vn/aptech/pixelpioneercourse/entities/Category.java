package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"category","lessons","reviews","createdAt","isPublished","frontPageImage","instructor","enrollments","tests","provider","grantedAuthorities","authorities","progresses","testFormat"})
    @ToString.Exclude
    private List<Course> courses;
    
    @JsonProperty("courseCount")
    public int getCourseCount() {
        return courses != null ? courses.size() : 0;
    }
}
