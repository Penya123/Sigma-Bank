package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
