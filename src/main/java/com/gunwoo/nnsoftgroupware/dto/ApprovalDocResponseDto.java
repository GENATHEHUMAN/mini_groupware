package com.gunwoo.nnsoftgroupware.dto;

import com.gunwoo.nnsoftgroupware.domain.ApprovalDoc;
import com.gunwoo.nnsoftgroupware.domain.ApprovalStatus;

import lombok.Getter;

@Getter
public class ApprovalDocResponseDto {

	private Long docId;
    private String title;
    private String content;
    private String drafterName;
    private String drafterUsername;
    private String approverName;
    private String approverUsername;
    private ApprovalStatus status;
	
    public ApprovalDocResponseDto(ApprovalDoc doc) {
        this.docId = doc.getDocId();
        this.title = doc.getTitle();
        this.content = doc.getContent(); 
        this.drafterName = doc.getDrafter().getName();
        this.drafterUsername = doc.getDrafter().getUsername(); 
        this.approverName = doc.getApprover().getName();
        this.approverUsername = doc.getApprover().getUsername(); 
        this.status = doc.getStatus();
    }
}
