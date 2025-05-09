package net.earelin.tasklist.domain.task;

import net.earelin.tasklist.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskListRepository extends MongoRepository<TaskList, String> {
  Page<TaskList> findAllByUser(Pageable pageable, User user);
}
