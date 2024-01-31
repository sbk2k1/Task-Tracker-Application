package tech.sbk2k1.tasktracker.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import tech.sbk2k1.tasktracker.model.ProjectDTO;

@Repository
public interface ProjectRepository extends MongoRepository<ProjectDTO, String> {

 Optional<ProjectDTO> findByName(String name);

}
