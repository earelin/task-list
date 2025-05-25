package net.earelin.tasklist.application.rest.user.dto;

public record CreateUserDto(
    String email,
    String firstname,
    String surname,
    String password) {}
