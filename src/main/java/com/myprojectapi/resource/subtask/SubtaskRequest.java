package com.myprojectapi.resource.subtask;

import com.myprojectapi.entity.SubtaskStatus;

public record SubtaskRequest(String title, SubtaskStatus status) {

}
