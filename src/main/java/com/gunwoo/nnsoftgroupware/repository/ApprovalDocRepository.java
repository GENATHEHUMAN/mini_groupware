package com.gunwoo.nnsoftgroupware.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunwoo.nnsoftgroupware.domain.ApprovalDoc;
import com.gunwoo.nnsoftgroupware.domain.User;

// ApprovalDoc Entity를 다루고, PK의 타입은 Long 이라고 명시
public interface ApprovalDocRepository extends JpaRepository<ApprovalDoc, Long>{

	// Spring Data JPA가 메소드 이름을 분석 -> 쿼리 자동 생성
	// Drafter를 기준으로 모든 결재 문서를 찾는 기능 만들어줌
	List<ApprovalDoc> findByDrafter(User drafter);
	
	
	
	// Approver를 기준으로 모든 결재 문서를 찾는 기능 만들어줌
	List<ApprovalDoc> findByApprover(User approver);
}
