package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Account;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,String> {

    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwnerUdi(String code, String udi);

    @Nullable List<Account> findAllByOwnerUdi(String udi);
}
