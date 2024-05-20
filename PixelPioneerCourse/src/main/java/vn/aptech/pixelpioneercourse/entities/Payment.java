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
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name= "payment_method", nullable = false, columnDefinition = "ENUM('CREDIT_CARD', 'PAYPAL')")
    private PaymentMethod paymentMethod;

    @OneToOne
    @JoinColumn(name = "enrollment_id",referencedColumnName = "id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

}

