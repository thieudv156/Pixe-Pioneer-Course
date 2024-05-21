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
@Table(name = "payment_details")
public class PaymentDetail {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "billing_id", referencedColumnName = "id")
    private Billing billing;

    @OneToOne
    @JoinColumn(name ="courese_id", referencedColumnName = "id", nullable = false, unique = true)
    private Course course;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "discount")
    private double discount;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;


}
