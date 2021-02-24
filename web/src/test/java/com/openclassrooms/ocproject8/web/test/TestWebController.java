package com.openclassrooms.ocproject8.web.test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.ocproject8.shared.service.UserService;
import com.openclassrooms.ocproject8.web.service.WebService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWebController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webContext;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private WebService webService;

	private static boolean initialised = false;

	@Before
	public void initialise() {
		if (!initialised) {
			userService.initializeUsers(100);
			initialised = true;
		}
	}
	
	@Before
	public void setupMockmvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
    
	//dont serialize tests
	// same as testRewardsController
}
