package pl.jstk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;

@Controller
public class BookSearchController {

	
	
	@Autowired
	private BookService bookService;
	
	
	//jaki szablon strony?
	//jakis formularz do wpisywania


	@RequestMapping(value = "/books/searchResultByTitle", method = RequestMethod.GET)
	public String showBooksByTitle(@RequestParam("title") String title, Model model){
		
		model.addAttribute("bookList", bookService.findBooksByTitle(title));
		return ViewNames.BOOKS;
		
	}
	
	
	@RequestMapping(value = "/books/searchResultByAuthors", method = RequestMethod.GET)
	public String showBooksByAuthors(@RequestParam("authors") String authors, Model model){
		
		model.addAttribute("bookList", bookService.findBooksByTitle(authors));
		return ViewNames.BOOKS;
		
	}
	
	
}
