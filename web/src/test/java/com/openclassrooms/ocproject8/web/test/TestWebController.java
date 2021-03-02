package com.openclassrooms.ocproject8.web.test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.ocproject8.shared.domain.UserEntity;
import com.openclassrooms.ocproject8.shared.domain.UserReward;
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
	
    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

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
	
    private ObjectMapper mapper = new ObjectMapper();
    
    @Before
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    	
	@Test                                                                                          
	public void getRewards() throws Exception {  
	      String userRewards = webService.getUserRewards("internalUser1");
	        mockServer.expect(ExpectedCount.once(),
	        requestTo(new URI("http://localhost:8080/getRewards")))
	        .andExpect(method(HttpMethod.GET))
	        .andRespond(withStatus(HttpStatus.OK)
	        .contentType(MediaType.APPLICATION_JSON)
	        .body(mapper.writeValueAsString(userRewards))
	      );                                  
	         
	String user = userRewards.toString();
	String uuid = user.getUserId().toString();
	String userName = user.getUserName();
	mockServer.verify();
	Assert.assertEquals(userRewards, userName);
	                                                     
	}
	// don't serialize tests
	// same as testRewardsController
}
