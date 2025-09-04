package com.gunwoo.nnsoftgroupware.dto;

import com.gunwoo.nnsoftgroupware.domain.TaskStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskStatusUpdateRequestDto {
	private TaskStatus status; // 변경할 새로운 상태 
}
