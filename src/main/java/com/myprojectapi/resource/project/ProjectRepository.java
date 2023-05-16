package com.myprojectapi.resource.project;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myprojectapi.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
