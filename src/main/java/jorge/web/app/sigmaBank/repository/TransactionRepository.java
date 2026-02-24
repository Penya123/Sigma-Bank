package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findAllByOwnerUdi(String udi, Pageable pageable);

    Page<Transaction> findAllByCardCardIdAndOwnerUdi(String cardId, String udi, Pageable pageable);

    Page<Transaction> findAllByAccountAccountIdAndOwnerUid(String accountId, String udi, Pageable pageable);
}
