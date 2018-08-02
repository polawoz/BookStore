package pl.jstk.controller;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.to.BookTo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	private static final String INFO_TEXT = "where you can find and exchange books with other users!";
	protected static final String WELCOME = "Welcome to Global Book Exchange";

	@GetMapping(value = "/")
	public String welcome(Model model) {
		model.addAttribute(ModelConstants.MESSAGE, WELCOME);
		model.addAttribute(ModelConstants.INFO, INFO_TEXT);
		return ViewNames.WELCOME;
	}

	@GetMapping(value = "/login")
	public String login(Model model) {

		return ViewNames.LOGIN;
	}



	@GetMapping(value = "/books/add")
	public String addBook(Model model) {
		model.addAttribute("newBook", new BookTo());
		return ViewNames.ADD_BOOK;
	}

	@GetMapping(value = "/books/search")
	public String showSearchView(Model model) {

		model.addAttribute("newBook", new BookTo());
		return "searchBook";

	}

	@GetMapping(value = "/403")
	public String show403Error(Model model) {

		model.addAttribute("error", "You have no access to this resource!");
		return "/403";

	}

}
