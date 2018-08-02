package pl.jstk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;

import pl.jstk.BookApplication;

import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookApplication.class)
@WebAppConfiguration
public class BooksControllerTest {

	List<BookTo> allBooksList;
	List<BookTo> booksByIdList;
	List<BookTo> allBooksListAfterDeletingID2;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Mock
	private BookService bookServiceMock;

	@Autowired
	private BooksController booksController;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(bookServiceMock);
		Mockito.reset(bookServiceMock);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain)
				.defaultRequest(get("/").with(testSecurityContext())).build();
		ReflectionTestUtils.setField(booksController, "bookService", bookServiceMock);

		allBooksList = new ArrayList<>();
		allBooksList.add(new BookTo(1L, "Ksiazka1", "Autor1", BookStatus.FREE));
		allBooksList.add(new BookTo(2L, "Ksiazka2", "Autor2", BookStatus.LOAN));
		allBooksList.add(new BookTo(3L, "Ksiazka3", "Autor3", BookStatus.FREE));
		allBooksList.add(new BookTo(4L, "Ksiazka4", "Autor4", BookStatus.MISSING));

		Mockito.when(bookServiceMock.findAllBooks()).thenReturn(allBooksList);

		booksByIdList = new ArrayList<>();
		booksByIdList.add(new BookTo(2L, "Ksiazka2", "Autor2", BookStatus.LOAN));
		Mockito.when(bookServiceMock.findBookById(2L)).thenReturn(booksByIdList);

	}

	@Test
	public void testShouldShowAllBooks() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(1)).findAllBooks();

		resultActions.andExpect(status().isOk()).andExpect(view().name("books"))
				.andExpect(model().attribute("bookList", allBooksList));

	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void testShouldShowBooksDetails() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=2"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(1)).findBookById(2L);

		resultActions.andExpect(status().isOk()).andExpect(view().name("book"))
				.andExpect(model().attribute("book", booksByIdList.get(0)));

	}

	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void testShouldDeleteBook() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/delete?id=1"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(1)).deleteBook(1L);

		resultActions.andExpect(status().isOk()).andExpect(view().name("books"))
				.andExpect(model().attribute("deleted", "yes"));

	}

	@Test
	public void testShouldNotDeleteBookAnonymous() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/delete?id=1"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(0)).deleteBook(1L);
		resultActions.andExpect(status().is3xxRedirection());

	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void testShouldNotDeleteBookByUser() throws Exception {

		// given when

		ResultActions resultActions = mockMvc.perform(get("/books/delete?id=1"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(0)).deleteBook(1L);
		resultActions.andExpect(status().is4xxClientError());

	}

	@Test
	public void testShouldNotShowDetailsAnonymous() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/book?id=2"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(0)).findBookById(2L);
		resultActions.andExpect(status().is3xxRedirection());

	}

	@Test
	public void testShowBooksByParameters() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(post("/books/searchByParam"));

		// then

		Mockito.verify(bookServiceMock, Mockito.times(1)).findBooksByTitleOrAuthor(Mockito.anyString(),
				Mockito.anyString());
		resultActions.andExpect(status().isOk()).andExpect(view().name("books"));

	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void testShouldNotAddBookEmptyTo() throws Exception {

		// given when
		ResultActions resultActions = mockMvc.perform(post("/greeting"));

		// then
		Mockito.verify(bookServiceMock, Mockito.times(0)).saveBook(null);
		resultActions.andExpect(status().is4xxClientError());

	}

	@Test
	@WithMockUser(username = "user", roles = "USER")
	public void testShouldAddBookByUser() throws Exception {

		// given when

		ResultActions resultActions = mockMvc
				.perform(post("/greeting").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.param("title", "Nowa ksiazka").param("authors", "Author").param("status", "FREE"));

		// then

		Mockito.verify(bookServiceMock, Mockito.times(1)).saveBook(Mockito.any());
		resultActions.andExpect(status().isOk()).andExpect(view().name("books"));

	}

}
