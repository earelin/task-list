package net.earelin.tasklist.application.rest.user;

import lombok.extern.slf4j.Slf4j;
import net.earelin.tasklist.application.rest.user.dto.CreateUserDto;
import net.earelin.tasklist.application.rest.user.dto.UserDto;
import net.earelin.tasklist.application.rest.user.dto.UserDtoMappers;
import net.earelin.tasklist.domain.user.User;
import net.earelin.tasklist.domain.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

  private final UserRepository userRepository;
  private final UserDtoMappers userDtoMappers;

  public UserController(UserRepository userRepository, UserDtoMappers userDtoMappers) {
    this.userRepository = userRepository;
    this.userDtoMappers = userDtoMappers;
  }

  @GetMapping
  public ResponseEntity<UserDto> findById(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(userDtoMappers.entityToDto(user));
  }

  @PostMapping
  public ResponseEntity<UserDto> create(@RequestBody CreateUserDto createUser) {
    var user = userDtoMappers.dtoToEntity(createUser);
    userRepository.save(user);

    log.atInfo()
        .addKeyValue("user", user)
        .log("User created: {}", user.getEmail());

    return new ResponseEntity<>(
        userDtoMappers.entityToDto(user), HttpStatus.CREATED);
  }

  @DeleteMapping()
  public ResponseEntity<Void> delete(@AuthenticationPrincipal User user) {
    userRepository.delete(user);

    log.atInfo()
        .addKeyValue("user", user)
        .log("User deleted: {}", user.getEmail());

    return ResponseEntity.noContent().build();
  }
}
