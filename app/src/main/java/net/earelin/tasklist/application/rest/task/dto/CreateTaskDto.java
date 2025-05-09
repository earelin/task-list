package net.earelin.tasklist.application.rest.task.dto;

import java.time.ZonedDateTime;

public record CreateTaskDto(
    String name,
    String description,
    ZonedDateTime deadline) {}
