package com.gunwoo.minigroupware.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunwoo.minigroupware.domain.Task;
import com.gunwoo.minigroupware.domain.User;

public interface TaskRepository extends JpaRepository<Task, Long> {

	// 담당자를 기준으로 모든 업무 카드를 찾는 기능 만들어줌
	List<Task> findByAssignee(User Assignee);
}
