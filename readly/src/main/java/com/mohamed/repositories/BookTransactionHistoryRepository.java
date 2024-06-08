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
                WHERE history.userId = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, String userId);

    @Query("""
                SELECT history
                FROM BookTransactionHistory history
                WHERE history.book.createdBy = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, String userId);

    @Query("""
                SELECT
                (COUNT(*) > 0) AS isBorrowed
                FROM BookTransactionHistory bookTransactionHistory
                WHERE bookTransactionHistory.userId = :userId
                AND bookTransactionHistory.book.id = :bookId
                AND bookTransactionHistory.returnedApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Integer bookId, String userId);

    @Query("""
                   SELECT history
                    FROM BookTransactionHistory history
                    WHERE history.book.id = :bookId
                    AND history.userId = :userId
                    AND history.returnedApproved = false
                    AND history.isReturned = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, String userId);

    @Query("""
                SELECT history
                        FROM BookTransactionHistory history
                        WHERE history.book.id = :bookId
                        AND history.book.createdBy = :userId
                        AND history.returnedApproved = true
                        AND history.isReturned = false
            """)
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, String userId);
}
