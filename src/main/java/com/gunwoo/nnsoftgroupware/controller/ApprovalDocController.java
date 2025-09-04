package com.gunwoo.nnsoftgroupware.controller;

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

import com.gunwoo.nnsoftgroupware.dto.ApprovalDocCreateRequestDto;
import com.gunwoo.nnsoftgroupware.dto.ApprovalDocResponseDto;
import com.gunwoo.nnsoftgroupware.service.ApprovalDocService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalDocController {

	private final ApprovalDocService approvalDocService;
	
	@GetMapping("/{docId}")
	public ResponseEntity<ApprovalDocResponseDto> getApprovalDoc(@PathVariable Long docId){
		ApprovalDocResponseDto responseDto = approvalDocService.getApprovalDoc(docId);
		return ResponseEntity.ok(responseDto);
	}
	
	@PostMapping
	public ResponseEntity<ApprovalDocResponseDto> createApprovalDoc(
			@RequestBody ApprovalDocCreateRequestDto requestDto,
			@AuthenticationPrincipal UserDetails userDetails) {
		
		ApprovalDocResponseDto responseDto = approvalDocService.createApprovalDoc(requestDto, userDetails);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}
	
	@GetMapping("/my-drafts")
	public ResponseEntity<List<ApprovalDocResponseDto>> getMyDrafts(@AuthenticationPrincipal UserDetails userDetails){
		List<ApprovalDocResponseDto> drafts = approvalDocService.getMyDrafts(userDetails);
        return ResponseEntity.ok(drafts); // 200 OK 상태 코드와 함께 DTO 리스트를 응답
	}
	
	@GetMapping("/to-me")
	public ResponseEntity<List<ApprovalDocResponseDto>> getDocsForMyApproval(@AuthenticationPrincipal UserDetails userDetails){
		List<ApprovalDocResponseDto> docs = approvalDocService.getDocsForMyApproval(userDetails);
		return ResponseEntity.ok(docs); // 200 OK 상태 코드와 함께 DTO 리스트를 응답
	}
	
	// 결재 문서 상신 API
    @PatchMapping("/{docId}/submit")
    public ResponseEntity<Void> submitDoc(@PathVariable Long docId, @AuthenticationPrincipal UserDetails userDetails) {
        approvalDocService.submitDoc(docId, userDetails);
        return ResponseEntity.ok().build(); // 성공 시 200 OK 응답
    }

    // 결재 문서 승인 API
    @PatchMapping("/{docId}/approve")
    public ResponseEntity<Void> approveDoc(@PathVariable Long docId, @AuthenticationPrincipal UserDetails userDetails) {
        approvalDocService.approveDoc(docId, userDetails);
        return ResponseEntity.ok().build();
    }

    // 결재 문서 반려 API
    @PatchMapping("/{docId}/reject")
    public ResponseEntity<Void> rejectDoc(@PathVariable Long docId, @AuthenticationPrincipal UserDetails userDetails) {
        approvalDocService.rejectDoc(docId, userDetails);
        return ResponseEntity.ok().build();
    }
}
