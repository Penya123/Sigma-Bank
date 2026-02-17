package jorge.web.app.sigmaBank.repository;

import jorge.web.app.sigmaBank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {

    User findByUsernameIgnoreCase(String username);
}
