package pl.jstk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller
public class BooksController {
	
	
	
	@Autowired
	private BookService bookService;
	

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String showAllBooks(Model model){
		
		model.addAttribute("bookList", bookService.findAllBooks());
		return ViewNames.BOOKS;
		
	}
	
	
	@RequestMapping(value = "/books/book", method = RequestMethod.GET)
	public String showDetails(@RequestParam("id") String bookId, Model model){
		
		List<BookTo> chosenBookInAList = bookService.findBookById(bookId);
		
		model.addAttribute("book", chosenBookInAList.get(0));

		
		return ViewNames.BOOK;
		
	}
		
	
//	@GetMapping(value = "/books")
//	public String booksPage(Model model) {
//// tu wywolac metode ktora zwraca liste ksiazek??
//		
////        model.addAttribute(ModelConstants.MESSAGE, WELCOME);
////        model.addAttribute(ModelConstants.INFO, INFO_TEXT);
//		return ViewNames.BOOKS;
//	}



}
