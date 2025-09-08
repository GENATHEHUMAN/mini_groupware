package com.gunwoo.minigroupware.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gunwoo.minigroupware.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	// Spring Data JPA는 메소드 이름을 분석하여 자동으로 쿼리를 생성해줌.
	// 아래 메소드는 'username' 필드로 사용자를 찾는 쿼리를 만들어줌. (로그인 기능에 사용)
	Optional<User> findByUsername(String username);

}
