package jorge.web.app.sigmaBank.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Card {
    private @Id @GeneratedValue(strategy = GenerationType.UUID)
    String cardId;

    @Column(nullable = false, unique = true)
    private Long cardNumber;

    private long cardHolder;
    private double balance;

    @CreationTimestamp
    private LocalDate iss;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime exp;
    private String cvv;
    private String pin;
    private String billingAddress;
    
}
