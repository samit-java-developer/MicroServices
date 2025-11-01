package com.samit.securitybasic;

import com.samit.securitybasic.entity.UserEntity;
import com.samit.securitybasic.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecuritybasicApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads() {
		UserEntity userEntity=new UserEntity(4L,"samit@gnmail.com","samit@123","samit");
		String token=jwtService.createToken(userEntity);
		System.out.println("Create Token: "+token);
		Long id=jwtService.generateUserIdFromToken(token);
		System.out.println("Generate Id from Token: "+id);
	}

}
