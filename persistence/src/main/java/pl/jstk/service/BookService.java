package pl.jstk.service;

import java.util.List;

import pl.jstk.to.BookTo;

public interface BookService {

    List<BookTo> findAllBooks();
    List<BookTo> findBooksByTitle(String title);
    List<BookTo> findBooksByAuthor(String author);
    //moje
    List<BookTo> findBookById(Long id);
    List<BookTo> findBooksByTitleOrAuthor(String title, String author);

    BookTo saveBook(BookTo book);
    void deleteBook(Long id);
}
