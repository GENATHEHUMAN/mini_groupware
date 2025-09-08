package com.gunwoo.minigroupware.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequestDto {
	
	private String username;
    private String password;
    private String name;
}
