package com.gunwoo.nnsoftgroupware.dto;

import com.gunwoo.nnsoftgroupware.domain.Task;
import com.gunwoo.nnsoftgroupware.domain.TaskStatus;

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