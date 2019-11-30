package com.java.mc.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mvc;
	private MockHttpSession session;

	@Before
	public void setupMockMvc(){
		this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
		session = new MockHttpSession();
		
	}
	
	@Test
	public void indexPageCanVisit() throws Exception {
		this.mvc.perform(MockMvcRequestBuilders.get("/").session(session))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
			;
	}
}
