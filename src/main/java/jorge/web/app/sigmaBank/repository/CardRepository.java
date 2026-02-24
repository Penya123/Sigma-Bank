package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {
    Optional<Card> findByOwnerUdi(String udi);

    boolean existsByCardNumber(long cardNumber);
}
