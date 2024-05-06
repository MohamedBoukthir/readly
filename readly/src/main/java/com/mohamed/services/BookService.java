package com.mohamed.services;

import com.mohamed.common.PageResponse;
import com.mohamed.entities.Book;
import com.mohamed.entities.BookTransactionHistory;
import com.mohamed.entities.User;
import com.mohamed.exception.OperationNotPermittedException;
import com.mohamed.file.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.mohamed.payload.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;
    private final FileStorageService fileStorageService;

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

    // Update the archived status of a book and return its ID
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

    // Borrow a book and return its ID
    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
        // Check if the book is archived or not shareable and throw an exception if true
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You are not allowed to borrow this book");
        }
        User user = ((User) connectedUser.getPrincipal());
        // Check if the user is the owner of the book or throw an exception if true
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to borrow your own book");
        }
        // Check if the book is already borrowed by the user or throw an exception if true
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository
                .isAlreadyBorrowedByUser(bookId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("You have already borrowed this book");
        }
        // Create a new book transaction history and save it to the database
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .isReturned(false)
                .returnedApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    // Return a borrowed book and return its ID
    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
        // Check if the book is archived or not shareable and throw an exception if true
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You are not allowed to borrow this book");
        }
        User user = ((User) connectedUser.getPrincipal());
        // Check if the user is the owner of the book or throw an exception if true
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to borrow or return your own book");
        }
        // Check if the book is already borrowed by the user
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndUserId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You have not borrowed this book"));
        // Update the book transaction history and save it to the database
        bookTransactionHistory.setReturned(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
        // Check if the book is archived or not shareable and throw an exception if true
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You are not allowed to borrow this book");
        }
        User user = ((User) connectedUser.getPrincipal());
        // Check if the user is the owner of the book or throw an exception if true
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You are not allowed to borrow or return your own book");
        }
        // Check if the book is already borrowed by the user
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository
                .findByBookIdAndOwnerId(bookId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet. You cannot approve the return"));
        // Update the book transaction history and save it to the database
        bookTransactionHistory.setReturnedApproved(true);
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }

    // Upload a book cover picture and save it to the database
    public void uploadBookCoverPic(MultipartFile file, Authentication connectedUser, Integer bookId) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));
        User user = ((User) connectedUser.getPrincipal());
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setCover(bookCover);
        bookRepository.save(book);
    }
}
