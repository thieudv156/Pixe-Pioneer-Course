package vn.aptech.pixelpioneercourse.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"enrollments", "password", "email", "roles", "createdAt", "updatedAt", "reviews", "discussions"})
    private User user;

    @Column(nullable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Column
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Boolean paymentStatus = false; // Indicates if payment is successful

    @Column(nullable = false)
    private Boolean subscriptionStatus = false; // Indicates if subscription is active

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType subscriptionType; // Monthly, Yearly, Unlimited

    @Column
    private LocalDateTime subscriptionEndDate; // Subscription end date
    
    @Setter
    @Transient
    private String formattedPaymentDate;
    
    @Setter
    @Transient
    private String formattedSubscriptionEndDate;
    
    public boolean isSubcriptionActive() {
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, subscriptionEndDate);
        return daysBetween > 0;
    }

    @Override
    public String toString() {
        return "Enrollment{id=" + id + ", enrolledAt=" + enrolledAt + ", paymentDate=" + paymentDate +
                ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus +
                ", subscriptionStatus=" + subscriptionStatus + ", subscriptionType=" + subscriptionType +
                ", subscriptionEndDate=" + subscriptionEndDate + "}";
    }

}
