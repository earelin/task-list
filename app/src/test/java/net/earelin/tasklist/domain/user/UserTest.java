package net.earelin.tasklist.domain.user;

import static net.earelin.tasklist.domain.user.UserFactory.USER_EMAIL;
import static net.earelin.tasklist.domain.user.UserFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {
  private User user;

  @BeforeEach
  void setUp() {
    user = createUser();
  }

  @Test
  void return_email_as_username() {
    assertThat(user.getUsername())
        .isEqualTo(USER_EMAIL);
  }
}
