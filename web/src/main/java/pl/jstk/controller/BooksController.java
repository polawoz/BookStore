package pl.jstk.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
public class BooksController {
	
	
	
	@Autowired
	private BookService bookService;
	

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String showAllBooks(Model model){
		
		model.addAttribute("bookList", bookService.findAllBooks());
		return ViewNames.BOOKS;
		
	}
	
	
	@RequestMapping(value = "/books/book", method = RequestMethod.GET)
	public String showDetails(@RequestParam("id") Long bookId, Model model){
		
		List<BookTo> chosenBookInAList = bookService.findBookById(bookId);
		
		model.addAttribute("book", chosenBookInAList.get(0));

		
		return ViewNames.BOOK;
		
	}
		
	
//	@RequestMapping(value="/greeting", method=RequestMethod.POST)
//	public String showViewAfterAddingBook(@RequestParam("title") @Valid String title, 
//			@RequestParam("authors") @Valid String authors,
//			@RequestParam("status") @Valid BookStatus status,
//			Model model){
//
//		
//		BookTo newBook = new BookTo(null, title, authors, status);
//		
//		bookService.saveBook(newBook);
//		
//		model.addAttribute("bookList", bookService.findAllBooks());
//		
//		return ViewNames.BOOKS;
//		
//	}
	
	
	
	@RequestMapping(value="/greeting", method=RequestMethod.POST)
	public String showViewAfterAddingBook(@ModelAttribute("newBook") @Valid BookTo newBook,
			Model model){
	
		
		bookService.saveBook(newBook);
		
		model.addAttribute("bookList", bookService.findAllBooks());
		
		return ViewNames.BOOKS;
		
	}
	
	
	@RequestMapping(value="/books/delete", method=RequestMethod.GET)
	public String showViewAfterDeletingBook(@RequestParam("id") Long bookId, Model model){
		
		bookService.deleteBook(bookId);
		model.addAttribute("bookList", bookService.findAllBooks());
		
		return ViewNames.BOOKS;		
	}
	
	




}
