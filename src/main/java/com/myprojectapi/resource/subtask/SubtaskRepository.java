package com.myprojectapi.resource.subtask;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myprojectapi.entity.Subtask;
import com.myprojectapi.entity.Task;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {

	List<Subtask> findByBelongTask(Task task);
	
	Integer countByBelongTask(Task task);

	Optional<Subtask> findByBelongTaskAndTitle(Task task, String title);

}
