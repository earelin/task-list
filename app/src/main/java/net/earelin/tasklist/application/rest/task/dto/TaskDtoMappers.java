package net.earelin.tasklist.application.rest.task.dto;

import net.earelin.tasklist.domain.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskDtoMappers {
  Task dtoToEntity(CreateTaskDto createTaskDto);

  Task updateEntityFromDto(UpdateTaskDto updateTaskDto, @MappingTarget Task task);

  TaskDto entityToDto(Task task);
}
