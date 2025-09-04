package com.gunwoo.nnsoftgroupware.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gunwoo.nnsoftgroupware.domain.User;
import com.gunwoo.nnsoftgroupware.dto.UserRegisterRequestDto;
import com.gunwoo.nnsoftgroupware.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserRegisterRequestDto requestDto){
		// @RequestBody: 요청의 Body에 담겨온 JSON 데이터를 DTO 객체로 변환해줍니다.
		
		userService.register(requestDto);
		
		return ResponseEntity.status(HttpStatus.CREATED)
							 .body("회원가입이 성공적으로 완료 되었습니다.");
	}
	
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	// GET /api/users/me 경로의 요청을 이 메소드가 처리합니다.
	@GetMapping("/me")
	public ResponseEntity<String> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
	    // @AuthenticationPrincipal: Spring Security가 현재 로그인한 사용자의 정보를
	    // UserDetails 객체에 담아 파라미터로 주입해주는 매우 편리한 어노테이션입니다.

	    String username = userDetails.getUsername();
	    
	    return ResponseEntity.ok("인증된 사용자입니다. 안녕하세요, " + username + "님!");
	}
	
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}
}
