package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.BookRequest;
import examination.teacherAndStudents.entity.Book;
import examination.teacherAndStudents.entity.BookBorrowing;

import java.util.List;

public interface LibraryService {
    Book addBook(BookRequest book);
    Book updateBookQuantity(Long bookId, int quantityToAdd);

    Book editBook(Long bookId, BookRequest updatedBook);

    void deleteBook(Long bookId);

    List<Book> getAllBooks();

    BookBorrowing borrowBook(Long studentId, Long bookId);

    BookBorrowing returnBook(Long borrowingId);
}

