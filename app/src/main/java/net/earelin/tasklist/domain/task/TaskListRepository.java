package net.earelin.tasklist.domain.task;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

public interface TaskListRepository extends FirestoreReactiveRepository<TaskList> {
  Flux<TaskList> findAll();
  
  default Page<TaskList> findAll(Pageable pageable) {
    return findAll()
        .collectList()
        .map(list -> {
          int start = (int) pageable.getOffset();
          int end = Math.min(start + pageable.getPageSize(), list.size());
          return new org.springframework.data.domain.PageImpl<>(
              list.subList(start, end), 
              pageable, 
              list.size()
          );
        })
        .block();
  }
}
