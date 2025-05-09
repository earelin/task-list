package net.earelin.tasklist.application.rest.user.dto;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

import net.earelin.tasklist.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
    componentModel = "spring",
    injectionStrategy = CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserDtoMappers {

  @Autowired
  protected PasswordEncoder passwordEncoder;

  @Mapping(
      target = "password",
      expression = "java(passwordEncoder.encode(createUserDto.password()))")
  public abstract User dtoToEntity(CreateUserDto createUserDto);

  public abstract UserDto entityToDto(User user);
}
