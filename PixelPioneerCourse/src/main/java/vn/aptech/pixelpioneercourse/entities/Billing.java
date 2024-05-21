package vn.aptech.pixelpioneercourse.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "billings")
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name= "payment_method", nullable = false, columnDefinition = "ENUM('CREDIT_CARD', 'PAYPAL')")
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime billDate;

    @Column(nullable = false)
    private double total;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentDetail> paymentDetails;

    @Column(name = "payment_status", nullable = false)
    private boolean paymentStatus;

}

