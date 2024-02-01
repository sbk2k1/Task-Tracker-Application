package tech.sbk2k1.tasktracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tech.sbk2k1.tasktracker.model.TaskDTO;

@Repository
public interface TaskRepository extends MongoRepository<TaskDTO, String> {

 Optional<TaskDTO> findByTitle(String title);

 List<TaskDTO> findAllByUsername(String username);

 // we need pagination here
 Page<TaskDTO> findAllByUsername(String username, Pageable pageable);

}
