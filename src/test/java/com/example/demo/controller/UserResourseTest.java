package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.service.UserService;
import com.example.demo.utility.JWTTokenProvider;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserResource.class)
public class UserResourseTest {
	
	@Autowired
	private MockMvc mockMvc; 
	
	@MockBean
	AuthenticationManager authenticationManager;
	
	@MockBean
	UserService userService;
	
	@MockBean
	JWTTokenProvider jwtTokenProvider;
	
	@Test
	public void test_Login() throws Exception {
		BDDMockito.when(authenticationManager.authenticate(BDDMockito.any())).thenReturn(BDDMockito.any());
		BDDMockito.when(userService.findUserByUsername(BDDMockito.anyString())).thenReturn(BDDMockito.any());
		mockMvc.perform(post("/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{username: superadmin, password: xyzxyz}"));
	}
	

}
