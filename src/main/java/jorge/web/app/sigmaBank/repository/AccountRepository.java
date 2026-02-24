package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {

    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwnerUdi(String code, String udi);

    List<Account> findAllByOwnerUdi(String udi);

    Optional<Account> findByCodeAndOwnerUdi(String code, String udi);

    Optional<Account> findByAccountNumber(long recipientAccountNumber);
}
