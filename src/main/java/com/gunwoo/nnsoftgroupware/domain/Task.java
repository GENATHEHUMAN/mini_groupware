package com.gunwoo.nnsoftgroupware.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_SEQ_GENERATOR")
	@SequenceGenerator(name = "TASK_SEQ_GENERATOR", sequenceName = "TASK_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "task_id")
	private Long taskId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignee_id", nullable = false)
	private User assignee;

	@Column(name = "title", nullable = false)
	private String title;

	@Lob
	@Column(name = "description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private TaskStatus status;

	@Builder
	public Task(User assignee, String title, String description) {
		this.assignee = assignee;
		this.title = title;
		this.description = description;
		this.status = TaskStatus.TODO; // 처음 생성 시 기본 상태는 '할 일'
	}

	// == 비즈니스 로직 ==//
	public void updateStatus(TaskStatus newStatus) {
		this.status = newStatus;
	}

}
