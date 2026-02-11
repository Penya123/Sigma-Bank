package jorge.web.app.sigmaBank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Card {
    private @Id @GeneratedValue(strategy = GenerationType.UUID)
    Long cardId;

    
}
