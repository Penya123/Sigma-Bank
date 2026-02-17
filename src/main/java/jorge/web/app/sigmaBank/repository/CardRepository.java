package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> {
}
