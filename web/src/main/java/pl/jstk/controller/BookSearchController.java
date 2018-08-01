package pl.jstk.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class BookSearchController {

	
	
	@Autowired
	private BookService bookService;
	
	
	
	@RequestMapping(value = "/books/searchByParam", method = RequestMethod.POST)
	public String showBooksByParameters(@RequestParam(value = "title", required=false, defaultValue="") String title,
			@RequestParam(value = "authors", required=false, defaultValue="") String authors,
			Model model){
		
		List<BookTo> resultList = bookService.findBooksByTitleOrAuthor(title, authors);
		
		model.addAttribute("bookList", resultList);

		return ViewNames.BOOKS;
		
	}
	

	
	
	
	
}
