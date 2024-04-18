package com.genest.app.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.genest.app.util.Constants;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new LoginController()).build();
	}

	@Test
	public void testShowLoginForm() throws Exception {
		mockMvc.perform(get("/login-form")).andExpect(status().isOk())
				.andExpect(view().name(Constants.LOGIN_FORM_VIEW));
	}

}