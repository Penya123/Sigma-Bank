package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
