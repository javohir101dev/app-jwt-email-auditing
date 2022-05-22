package emailauditing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import emailauditing.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository(value = "userRepo")
public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndCode(String email, String code);

    Optional<User> findByEmail(String email);



}
