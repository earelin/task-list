package net.earelin.tasklist.application.rest.task.dto;

import java.time.ZonedDateTime;
import java.util.Set;

public record TaskDto(
    Long id,
    String name,
    String description,
    Boolean completed,
    ZonedDateTime deadline,
    Integer order,
    Set<String> tags
) {}
