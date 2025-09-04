package com.gunwoo.nnsoftgroupware.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreateRequestDto {
	private String title;
	private String description;
	private Long assigneeId; // 업무 담당자의 User ID
}
