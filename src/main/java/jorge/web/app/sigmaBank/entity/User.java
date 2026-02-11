package jorge.web.app.sigmaBank.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
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

    @ManyToMany
    private List<String> roles;

}
