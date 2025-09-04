package com.gunwoo.nnsoftgroupware.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunwoo.nnsoftgroupware.domain.Task;
import com.gunwoo.nnsoftgroupware.domain.User;
import com.gunwoo.nnsoftgroupware.dto.TaskCreateRequestDto;
import com.gunwoo.nnsoftgroupware.dto.TaskResponseDto;
import com.gunwoo.nnsoftgroupware.dto.TaskStatusUpdateRequestDto;
import com.gunwoo.nnsoftgroupware.repository.TaskRepository;
import com.gunwoo.nnsoftgroupware.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponseDto createTask(TaskCreateRequestDto requestDto, UserDetails userDetails) {
        // 담당자 정보 찾기
        User assignee = userRepository.findById(requestDto.getAssigneeId())
                .orElseThrow(() -> new IllegalArgumentException("담당자를 찾을 수 없습니다."));

        // Task Entity 생성
        Task newTask = Task.builder()
                .assignee(assignee)
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .build();

        // DB에 저장
        Task savedTask = taskRepository.save(newTask);

        // Response DTO로 변환하여 반환
        return new TaskResponseDto(savedTask);
    }
    
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getMyTasks(UserDetails userDetails) {
        // 현재 로그인한 사용자(담당자) 정보 찾기
        User assignee = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // Repository를 이용해 해당 담당자에게 할당된 모든 업무를 찾아 DTO 리스트로 변환 후 반환
        return taskRepository.findByAssignee(assignee)
                .stream()
                .map(TaskResponseDto::new)
                .collect(Collectors.toList());
    }
    
    public void updateTaskStatus(Long taskId, TaskStatusUpdateRequestDto requestDto, UserDetails userDetails) {
        // 현재 사용자 정보 찾기
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 업무 정보 찾기
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("업무를 찾을 수 없습니다."));

        // 권한 확인: 현재 사용자가 업무 담당자인지 확인
        if (!task.getAssignee().equals(currentUser)) {
            throw new AccessDeniedException("업무 상태를 변경할 권한이 없습니다.");
        }

        // Entity의 상태 변경 메소드 호출
        task.updateStatus(requestDto.getStatus());
    }
}
