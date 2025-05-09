package net.earelin.tasklist.domain.user;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Document
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class User implements UserDetails {
  @Id
  @EqualsAndHashCode.Include
  private final String id;

  @Indexed(unique = true)
  private String email;

  private String firstName;
  private String surname;
  private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getUsername() {
    return email;
  }
}
