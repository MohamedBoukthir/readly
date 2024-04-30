package com.mohamed.services;

import com.mohamed.common.PageResponse;
import com.mohamed.entities.Book;
import com.mohamed.entities.BookTransactionHistory;
import com.mohamed.entities.User;
import com.mohamed.exception.OperationNotPermittedException;
import com.mohamed.payload.book.BookMapper;
import com.mohamed.payload.book.BookRequest;
import com.mohamed.payload.book.BookResponse;
import com.mohamed.payload.book.BorrowedBookResponse;
import com.mohamed.repositories.BookRepository;
import com.mohamed.repositories.BookTransactionHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.mohamed.payload.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;

    // Save a book and return its ID
    public Integer save(BookRequest bookRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }

    // Find a book by ID or throw an exception if not found
    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
    }

    // Find all books and return them as a paginated response (10 books per page)
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        // Sort books by creation date in descending order and find displayable books
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // Find all displayable books (books that are not archived and are shareable)
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());

        // Convert books to book responses and return them as a paginated response
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    // Find all books by owner and return them as a paginated response (10 books per page)
    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        // Sort books by creation date in descending order and find books by owner
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);

        // Convert books to book responses and return them as a paginated response
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    // Find all borrowed books and return them as a paginated response (10 books per page)
    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        // Sort books by creation date in descending order and find books by owner
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // Find all borrowed books by user ID
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository
                .findAllBorrowedBooks(pageable, user.getId());
        // Convert borrowed books to borrowed book responses and return them as a paginated response
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    // Find all returned books and return them as a paginated response (10 books per page)
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        // Sort books by creation date in descending order and find books by owner
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        // Find all borrowed books by user ID
        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository
                .findAllReturnedBooks(pageable, user.getId());
        // Convert borrowed books to borrowed book responses and return them as a paginated response
        List<BorrowedBookResponse> bookResponses = allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    // Update the shareable status of a book and return its ID
    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
        User user = ((User) connectedUser.getPrincipal());
        // Check if the user is the owner of the book or throw an exception if not
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to update the shareable status of the book");
        }
        // Update the shareable status of the book and save it to the database
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
        User user = ((User) connectedUser.getPrincipal());
        // Check if the user is the owner of the book or throw an exception if not
        if (!Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to update the archived status of the book");
        }
        // Update the shareable status of the book and save it to the database
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }
}
