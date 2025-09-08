package com.gunwoo.minigroupware.service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gunwoo.minigroupware.domain.ApprovalDoc;
import com.gunwoo.minigroupware.domain.User;
import com.gunwoo.minigroupware.dto.ApprovalDocCreateRequestDto;
import com.gunwoo.minigroupware.dto.ApprovalDocResponseDto;
import com.gunwoo.minigroupware.repository.ApprovalDocRepository;
import com.gunwoo.minigroupware.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalDocService {

	private final ApprovalDocRepository approvalDocRepository;
	private final UserRepository userRepository;
	
	// 상세보기 로직
	public ApprovalDocResponseDto getApprovalDoc(Long docId) {
		ApprovalDoc doc = approvalDocRepository.findById(docId)
				.orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
		
		return new ApprovalDocResponseDto(doc);
	}
	
	@Transactional
	public ApprovalDocResponseDto createApprovalDoc(ApprovalDocCreateRequestDto requestDto, UserDetails userDetails) {
		// 1. 기안자 찾기
		String username = userDetails.getUsername();
		User drafter = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("기안자를 찾을 수 없습니다."));
	
		// 2. 결재자 정보 찾기
		User approver = userRepository.findById(requestDto.getApproverId())
				.orElseThrow(() -> new IllegalArgumentException("결재자를 찾을 수 없습니다."));
		
		// 3. ApprovalDoc Entity 생성
		ApprovalDoc newDoc = ApprovalDoc.builder()
				.drafter(drafter)
				.approver(approver)
				.title(requestDto.getTitle())
				.content(requestDto.getContent())
				.build();
		
		
		/*
		 * System.out.println("--- DB 저장 전 Content 값 확인 ---");
		 * System.out.println(newDoc.getContent());
		 * System.out.println("---------------------------------");
		 */
		
		// 4. DB에 저장
		ApprovalDoc savedDoc = approvalDocRepository.save(newDoc);
		
		// 5. Response DTO로 변환
		return new ApprovalDocResponseDto(savedDoc);
	}
	
	// 내가 올린 결재서류 조회
	public List<ApprovalDocResponseDto> getMyDrafts(UserDetails userDetails){
		// 현재 로그인한 사용자 정보 찾기
		User drafter = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		
		// Repository를 이용해 해당 사용자가 기안한 모든 문서를 찾아서 DTO 리스트로 변환 후 반환
		return approvalDocRepository.findByDrafter(drafter)
				.stream()
				.map(ApprovalDocResponseDto::new) //  Stream API를 사용하여 각 ApprovalDoc을 DTO로 변환
				.collect(Collectors.toList());
	}
	
	// 내가 결재할 문서 목록 조회
	public List<ApprovalDocResponseDto> getDocsForMyApproval(UserDetails userDetails){
		// 로그인 유저 찾기
		User approver = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
		
		// Repository를 이용해 해당 사용자에게 온 모든 결재 문서를 찾아서 DTO 리스트로 변환 후 반환
        return approvalDocRepository.findByApprover(approver)
        		.stream()
				.map(ApprovalDocResponseDto::new) //  Stream API를 사용하여 각 ApprovalDoc을 DTO로 변환
				.collect(Collectors.toList());
	}
	
	
	// 결재 서류 상신
	@Transactional
	public void submitDoc(Long docId, UserDetails userDetails) {
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("사용자를 차을 수 없습니다."));
		
		ApprovalDoc doc = approvalDocRepository.findById(docId)
				.orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));
		
		// 권한 확인 : 현재 사용자가 '기안자'인지 확인
		if(!doc.getDrafter().equals(currentUser)) {
			throw new AccessDeniedException("문서를 상신할 권한이 없습니다.");
		}
		
		doc.submit();
	}
	
	// 결재 문서 승인
    @Transactional
    public void approveDoc(Long docId, UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ApprovalDoc doc = approvalDocRepository.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));

        // 권한 확인: 현재 사용자가 결재자인지 확인
        if (!doc.getApprover().equals(currentUser)) {
            throw new AccessDeniedException("문서를 승인할 권한이 없습니다.");
        }

        doc.approve();
    }
	
	// 결재 문서 반려
    @Transactional
    public void rejectDoc(Long docId, UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ApprovalDoc doc = approvalDocRepository.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("문서를 찾을 수 없습니다."));

        // 권한 확인: 현재 사용자가 결재자인지 확인
        if (!doc.getApprover().equals(currentUser)) {
            throw new AccessDeniedException("문서를 반려할 권한이 없습니다.");
        }

        doc.reject();
    }
	
}
