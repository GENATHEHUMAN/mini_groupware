package com.gunwoo.minigroupware.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 해당 클래스가 DB 테이블과 매핑되는 Entity 라는 것을 명시
@Getter
@NoArgsConstructor // 파라미터 없는 기본 생성자를 보호된 접근 수준으로 생성.
@Table(name = "USERS") // DB에 'USERS'라는 이름의 테이블로 생성.
public class User {

	@Id // 해당 테이블의 PK
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR") // Oracle의 SEQUENCE를 사용하여 PK
																							// 값을 자동 생성
	@SequenceGenerator(name = "USER_SEQ_GENERATOR", sequenceName = "USER_SEQ", initialValue = 1, allocationSize = 1)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username", nullable = false, unique = true) // nullable=false: NOT NULL 제약조건, unique=true: UNIQUE
																// 제약조건
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Builder
	public User(String username, String password, String name) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
	}

}
