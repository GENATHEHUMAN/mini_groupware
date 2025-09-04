package com.gunwoo.nnsoftgroupware.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalDoc { // 테이블명 자동 생성

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPROVAL_DOC_SEQ_GENERATOR")
	@SequenceGenerator(name = "APPROVAL_DOC_SEQ_GENERATOR", sequenceName = "APPROVAL_DOC_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "doc_id")
	private Long docId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "drafter_id", nullable = false)
	private User drafter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approver_id", nullable = false)
	private User approver;

	@Column(name = "title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "content", nullable = false)
	private String content;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ApprovalStatus status;

	@Builder
	public ApprovalDoc(User drafter, User approver, String title, String content) {
		this.drafter = drafter;
		this.approver = approver;
		this.title = title;
		this.content = content;
		this.status = ApprovalStatus.DRAFT;
	}
	
	public void submit() {
		this.status = ApprovalStatus.PENDING;
	}

	public void approve() {
		this.status = ApprovalStatus.APPROVED;
	}
	
	public void reject() {
		this.status = ApprovalStatus.REJECTED;
	}
	
}
