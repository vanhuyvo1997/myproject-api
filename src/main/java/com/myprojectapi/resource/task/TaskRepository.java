package com.myprojectapi.resource.task;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myprojectapi.entity.Project;
import com.myprojectapi.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	Page<Task> findByBelongProjectAndTitleIgnoreCaseContaining(Project project, String term, Pageable page);

	Optional<Task> findByBelongProjectAndTitleIgnoreCase(Project project, String title);

}
