package net.earelin.tasklist.application.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CachedPasswordEncoder implements PasswordEncoder {
  private final PasswordEncoder passwordEncoder;

  public CachedPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String encode(CharSequence rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  @Override
  @Cacheable(value = "passwords")
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
