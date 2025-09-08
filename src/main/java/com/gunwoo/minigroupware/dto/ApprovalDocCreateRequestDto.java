package com.gunwoo.minigroupware.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApprovalDocCreateRequestDto {

	private String title;
	private String content;
	private Long approverId; // 결재자의 User Id
	
}
