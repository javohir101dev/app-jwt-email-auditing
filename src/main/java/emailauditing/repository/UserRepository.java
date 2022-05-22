package emailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import emailauditing.entity.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

}
