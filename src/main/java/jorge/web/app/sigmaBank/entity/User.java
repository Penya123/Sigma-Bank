package jorge.web.app.sigmaBank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_user")
public class User {
    private @Id @GeneratedValue(strategy = GenerationType.UUID)
    String udi;

    private String firstName;
    private String lastName;
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    private String tag;
    private Date dob;
    private String gender;
    private String tel;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @CreationTimestamp
    private LocalDateTime updatedAt;
    private List<String> roles;

    @OneToOne(mappedBy = "owner")
    private Card card;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> Transactions;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;
}
