package net.earelin.tasklist.domain.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such task")
public class TaskNotFound extends RuntimeException {}
