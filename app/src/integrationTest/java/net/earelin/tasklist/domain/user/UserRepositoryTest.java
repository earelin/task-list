package net.earelin.tasklist.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserRepositoryTest {

  public static final String USER_EMAIL = "test@example.com";
  public static final String USER_FIRSTNAME = "Test";
  public static final String USER_SURNAME = "User";
  public static final String USER_PASSWORD = "user-secret";
  public static final String DELETE_USER_EMAIL = "delete@example.com";
  public static final String DELETE_USER_FIRSTNAME = "Delete";
  public static final String DELETE_USER_SURNAME = "User";
  public static final String DELETE_USER_PASSWORD = "delete-user-secret";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @AfterEach
  void tearDown() {
    mongoTemplate.dropCollection(User.class);
  }

  @Test
  void create_and_find_an_user() {
    User user = new User(null, USER_EMAIL, USER_FIRSTNAME, USER_SURNAME, USER_PASSWORD);

    userRepository.save(user);
    Optional<User> found = userRepository.findByEmail(USER_EMAIL);

    assertThat(found)
        .isPresent()
        .get()
        .extracting(User::getEmail, User::getFirstname, User::getSurname, User::getPassword)
        .containsExactly(USER_EMAIL, USER_FIRSTNAME, USER_SURNAME, USER_PASSWORD);
  }

  @Test
  void delete_an_user() {
    User user = new User(null, DELETE_USER_EMAIL, DELETE_USER_FIRSTNAME, DELETE_USER_SURNAME, DELETE_USER_PASSWORD);
    var createdUser = userRepository.save(user);

    userRepository.delete(createdUser);

    var found = userRepository.findByEmail("delete@example.com");
    assertThat(found)
        .isNotPresent();
  }
}
