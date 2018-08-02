package pl.jstk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class BooksController {

	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String showAllBooks(Model model) {

		model.addAttribute("bookList", bookService.findAllBooks());
		return ViewNames.BOOKS;

	}

	@RequestMapping(value = "/books/book", method = RequestMethod.GET)
	public String showDetails(@RequestParam("id") Long bookId, Model model) {

		List<BookTo> chosenBookInAList = bookService.findBookById(bookId);

		model.addAttribute("book", chosenBookInAList.get(0));

		return ViewNames.BOOK;

	}

	@RequestMapping(value = "/books/searchByParam", method = RequestMethod.POST)
	public String showBooksByParameters(
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "authors", required = false, defaultValue = "") String authors, Model model) {

		List<BookTo> resultList = bookService.findBooksByTitleOrAuthor(title, authors);

		model.addAttribute("bookList", resultList);

		return ViewNames.BOOKS;

	}

	@RequestMapping(value = "/greeting", method = RequestMethod.POST)
	public String showViewAfterAddingBook(@ModelAttribute("newBook") @Valid BookTo newBook, Model model) {

		bookService.saveBook(newBook);

		model.addAttribute("bookList", bookService.findAllBooks());

		return ViewNames.BOOKS;

	}

	@Secured(value = "ROLE_ADMIN")
	@RequestMapping(value = "/books/delete", method = RequestMethod.GET)
	public String showViewAfterDeletingBook(@RequestParam("id") Long bookId, Model model) {

		bookService.deleteBook(bookId);
		model.addAttribute("bookList", bookService.findAllBooks());
		model.addAttribute("deleted", "yes");

		return ViewNames.BOOKS;
	}

}
