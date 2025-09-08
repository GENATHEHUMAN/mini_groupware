package com.gunwoo.minigroupware.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gunwoo.minigroupware.dto.TaskCreateRequestDto;
import com.gunwoo.minigroupware.dto.TaskResponseDto;
import com.gunwoo.minigroupware.dto.TaskStatusUpdateRequestDto;
import com.gunwoo.minigroupware.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;
	
	@PostMapping
	public ResponseEntity<TaskResponseDto> createTask(
			@RequestBody TaskCreateRequestDto requestDto,
			@AuthenticationPrincipal UserDetails userDetails
			){
		
		TaskResponseDto responseDto = taskService.createTask(requestDto, userDetails);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
	
	@GetMapping("/my-tasks")
    public ResponseEntity<List<TaskResponseDto>> getMyTasks(@AuthenticationPrincipal UserDetails userDetails) {
        List<TaskResponseDto> myTasks = taskService.getMyTasks(userDetails);
        return ResponseEntity.ok(myTasks);
    }
	
	@PatchMapping("/{taskId}/status")
    public ResponseEntity<Void> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody TaskStatusUpdateRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        taskService.updateTaskStatus(taskId, requestDto, userDetails);
        return ResponseEntity.ok().build();
    }
	
}
