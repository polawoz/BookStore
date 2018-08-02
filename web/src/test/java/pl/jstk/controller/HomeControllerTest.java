package pl.jstk.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.jstk.BookApplication;
import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookApplication.class)
@WebAppConfiguration
public class HomeControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private HomeController homeController;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Before
	public void setUp() throws Exception {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain)
				.defaultRequest(get("/").with(testSecurityContext())).build();

	}

	@Test
	public void testWelcome() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name("welcome"))
				.andExpect(model().attribute(ModelConstants.MESSAGE, HomeController.WELCOME))
				.andExpect(content().string(containsString("")));

	}

	@Test
	public void testLogin() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/login"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name("login"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void testAddBook() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/add"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.ADD_BOOK));
	}

	@Test
	public void testShowSearchView() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/search"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name("searchBook"));
	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void testShow403Error() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/403"));

		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name("/403"));

	}

}
