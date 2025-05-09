package net.earelin.tasklist.application.rest.tasklist.dto;

import java.util.Set;
import net.earelin.tasklist.application.rest.task.dto.TaskDto;

public record TaskListDto(
    String id,
    String name,
    String description,
    Set<TaskDto> tasks) {}
