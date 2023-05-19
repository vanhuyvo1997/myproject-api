package com.myprojectapi.resource.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.myprojectapi.entity.Project;
import com.myprojectapi.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	Optional<Project> findByNameAndOwner(String name, User owner);

	Page<Project> findByOwner(User owner,Pageable page);

}
