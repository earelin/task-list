package net.earelin.tasklist.application.rest.user.dto;

public record CreateUserDto(
    String email,
    String name,
    String surname,
    String password) {}
