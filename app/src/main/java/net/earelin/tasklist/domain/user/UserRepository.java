package net.earelin.tasklist.domain.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
  Optional<User> findByEmail(String email);

  void deleteByEmail(String email);
}
