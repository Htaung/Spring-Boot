package org.test.bookpub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.test.bookpub.entity.Book;
import org.test.bookpub.entity.Isbn;
import org.test.bookpub.entity.IsbnEditor;
import org.test.bookpub.entity.Reviewer;
import org.test.bookpub.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {
	@Autowired
	private BookRepository bookRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@RequestMapping(value = "/{isbn}", method = RequestMethod.GET)
	public Book getBook(@PathVariable Isbn isbn) {
		return bookRepository.findBookByIsbn(isbn.getIsbn());
	}

	@RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
	public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
		return book.getReviewers();
	}

//	@RequestMapping(value = "/session", method = RequestMethod.GET)
//	public String getSessionId(HttpServletRequest request) {
//		return request.getSession().getId();
//	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Isbn.class, new IsbnEditor());
	}
}