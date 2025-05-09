package net.earelin.tasklist.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void get_user_by_username() {
    final var user = UserFactory.createUser();
    when(userRepository.findByEmail(user.getId()))
        .thenReturn(Optional.of(user));

    var returnedUser = userService.loadUserByUsername(user.getId());

    assertThat(returnedUser)
        .isEqualTo(user);
  }

  @Test
  void not_found_user_by_username() {
    when(userRepository.findByEmail(any()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.loadUserByUsername("nobody@example.com"))
        .isInstanceOf(UsernameNotFoundException.class);
  }

}
