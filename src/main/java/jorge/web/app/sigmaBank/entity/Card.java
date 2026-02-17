package jorge.web.app.sigmaBank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private @Id @GeneratedValue(strategy = GenerationType.UUID)
    String cardId;

    @Column(nullable = false, unique = true)
    private long cardNumber;

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
    @OneToOne
    @JoinColumn(name = "owner-id")
    private User owner;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transaction;
}
