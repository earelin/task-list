package net.earelin.tasklist.application.rest.tasklist.dto;

import net.earelin.tasklist.domain.task.TaskList;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TaskListDtoMappers {
  public abstract TaskList dtoToEntity(ModifyTaskListDto createTaskListDto);

  public abstract TaskList updateEntityFromDto(
      ModifyTaskListDto updateTaskListDto, @MappingTarget TaskList taskList);

  public abstract TaskListDto entityToDto(TaskList taskList);

  public Page<TaskListDto> entityToDtoPage(Page<TaskList> taskList) {
    var taskListsDto = taskList.getContent().stream()
        .map(this::entityToDto)
        .toList();
    return new PageImpl<>(taskListsDto, taskList.getPageable(), taskList.getTotalElements());
  }
}
