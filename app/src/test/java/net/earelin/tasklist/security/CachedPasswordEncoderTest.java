package net.earelin.tasklist.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.earelin.tasklist.application.security.CachedPasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CachedPasswordEncoderTest {

  private static final String PASSWORD = "password";
  private static final String ENCODED_PASSWORD = "encodedPassword";

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private CachedPasswordEncoder cachedPasswordEncoder;

  @Test
  void encode_password() {
    when(passwordEncoder.encode(PASSWORD))
        .thenReturn(ENCODED_PASSWORD);

    assertThat(passwordEncoder.encode(PASSWORD))
        .isEqualTo(ENCODED_PASSWORD);
  }

  @Test
  void match_password() {
    when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD))
        .thenReturn(true);

    assertThat(cachedPasswordEncoder.matches(PASSWORD, ENCODED_PASSWORD))
        .isTrue();
  }
}
