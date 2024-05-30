package vn.aptech.pixelpioneercourse.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name="progress")
    private Integer progress = 0;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    @Column
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentMethod paymentMethod;
}
