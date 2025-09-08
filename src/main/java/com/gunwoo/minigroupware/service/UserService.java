package com.gunwoo.minigroupware.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gunwoo.minigroupware.domain.User;
import com.gunwoo.minigroupware.dto.UserRegisterRequestDto;
import com.gunwoo.minigroupware.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void register(UserRegisterRequestDto requestDto) {
		
		// 1. 사용자 이름 중복 체크
		userRepository.findByUsername(requestDto.getUsername())
			.ifPresent(user -> {
				throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
			});
		
		// 2. 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
		
		// 3. User Entity 생성 및 저장
		User newUser = User.builder()
				.username(requestDto.getUsername())
				.password(encodedPassword)
				.name(requestDto.getName())
				.build();
		
		userRepository.save(newUser);
	}
	
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
