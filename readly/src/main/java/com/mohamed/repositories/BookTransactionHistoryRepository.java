package com.mohamed.repositories;

import com.mohamed.entities.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

    @Query("""
        SELECT history
        FROM BookTransactionHistory history
        WHERE history.user.id = :userId
    """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

    @Query("""
        SELECT history
        FROM BookTransactionHistory history
        WHERE history.book.owner.id = :userId
    """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

    @Query("""
        SELECT
        (COUNT(*) > 0) AS isBorrowed
        FROM BookTransactionHistory bookTransactionHistory
        WHERE bookTransactionHistory.user.id = :userId
        AND bookTransactionHistory.book.id = :bookId
        AND bookTransactionHistory.returnedApproved = false
    """)
    boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

    @Query("""
           SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.id = :bookId
            AND history.user.id = :userId
            AND history.returnedApproved = false
            AND history.isReturned = false
    """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query("""
    SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.id = :bookId
            AND history.book.owner.id = :userId
            AND history.returnedApproved = true
            AND history.isReturned = false
""")
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}
