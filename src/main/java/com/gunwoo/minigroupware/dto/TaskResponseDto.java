package com.gunwoo.minigroupware.dto;

import com.gunwoo.minigroupware.domain.Task;
import com.gunwoo.minigroupware.domain.TaskStatus;

import lombok.Getter;

@Getter
public class TaskResponseDto {
    private Long taskId;
    private String title;
    private String assigneeName; // 담당자 이름
    private TaskStatus status;

    public TaskResponseDto(Task task) {
        this.taskId = task.getTaskId();
        this.title = task.getTitle();
        this.assigneeName = task.getAssignee().getName();
        this.status = task.getStatus();
    }
}