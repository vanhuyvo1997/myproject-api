package com.myprojectapi.resource.project;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.myprojectapi.entity.Project;
import com.myprojectapi.entity.ProjectStatus;
import com.myprojectapi.entity.User;
import com.myprojectapi.resource.project.exceptions.ProjectAlreadyExistedException;
import com.myprojectapi.resource.project.exceptions.ProjectNameConflictException;
import com.myprojectapi.resource.project.exceptions.ProjectNotFountException;
import com.myprojectapi.util.PageResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProjectService {
	
	private final ProjectRepository projRepo;

	public ProjectDTO create(ProjectRequest rq) {
		var owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(projRepo.findByNameAndOwnerAndDeletedIsFalse(rq.name(), owner).isPresent()) {
			throw new ProjectAlreadyExistedException(rq.name() + " already exist");
		};
		
		Project p = Project.builder()
				.name(rq.name())
				.owner(owner)
				.startedAt(LocalDateTime.now())
				.status(ProjectStatus.NEW)
				.build();
		p.setName(rq.name());
		p = projRepo.save(p);
		return ProjectDTO.from(p);
	}

	public PageResponse<ProjectDTO> getPage(int pageNum, int size, boolean isDescending, String term, String ...sortProperties) {
		var owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Sort sort = Sort.by(sortProperties);
		if(isDescending) sort = sort.descending();
		Pageable page = PageRequest.of(pageNum, size, sort);
		var currentPage = projRepo.findByOwnerAndNameIgnoreCaseContainingAndDeletedIsFalse(owner, term, page);
		return new PageResponse<>(
				currentPage.getTotalPages(),
				currentPage.getNumber(),
				currentPage.getContent().stream().map(ProjectDTO::from).toList());
	}

	public void delete(Long id) {
		var owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		var project = projRepo.findById(id).orElseThrow(()->new ProjectNotFountException("project id=" + id + " Not found"));
		if(project.isDeleted() || !owner.equals(project.getOwner())) {
			throw new ProjectNotFountException("project id=" + id + " Not found");
		} else {
			project.setDeleted(true);
			projRepo.save(project);
		}
	}

	public void updateName(Long id, String newName) {
		var owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(projRepo.findByNameAndOwnerAndDeletedIsFalse(newName, owner).isPresent()) {
			throw new ProjectNameConflictException(newName + " is already in use");
		}
		
		var project = projRepo.findByIdAndOwnerAndDeletedIsFalse(id, owner).orElseThrow(()->new ProjectNotFountException("project id=" + id + " Not found"));
		if(!newName.equals(project.getName())) {
			project.setName(newName);
			projRepo.save(project);
		}
	}
	
}
