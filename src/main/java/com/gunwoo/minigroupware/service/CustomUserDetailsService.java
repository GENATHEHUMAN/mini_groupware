package com.gunwoo.minigroupware.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gunwoo.minigroupware.domain.User;
import com.gunwoo.minigroupware.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		// 1. DB에서 username으로 사용자 정보 찾기
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
		
		// 2. 찾은 사용자 정보를 Spring Security를 위해 UserDetails객체로 변환
		return new org.springframework.security.core.userdetails.User(
			user.getUsername(),
			user.getPassword(), // 암호화된 비밀번호
			new ArrayList<>() // 사용자의 권한 목록
		);
	}
}
