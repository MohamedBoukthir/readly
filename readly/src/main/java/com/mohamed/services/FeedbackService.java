package com.mohamed.services;

import com.mohamed.common.PageResponse;
import com.mohamed.entities.Book;
import com.mohamed.entities.Feedback;
import com.mohamed.entities.User;
import com.mohamed.exception.OperationNotPermittedException;
import com.mohamed.payload.feedback.FeedbackMapper;
import com.mohamed.payload.feedback.FeedbackRequest;
import com.mohamed.payload.feedback.FeedbackResponse;
import com.mohamed.repositories.BookRepository;
import com.mohamed.repositories.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    // Save feedback
    public Integer saveFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        // Find the book by ID or throw an exception if not found
        Book book = bookRepository.findById(feedbackRequest.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + feedbackRequest.bookId()));
        // Check if the book is archived or not shareable and throw an exception if true
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("You are not allowed to give feedback on this book");
        }
//        User user = ((User) connectedUser.getPrincipal());
        // Check if the user is the owner of the book or throw an exception if true
        if (Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            throw new OperationNotPermittedException("You are not allowed to give feedback on your own book");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    // Find all feedbacks by book
    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        // Create a pageable object using the page and size parameters
        Pageable pageable = PageRequest.of(page, size);
        // Get the connected user object from the authentication object and cast it to a user object
        User user = ((User) connectedUser.getPrincipal());
        // Find all feedbacks by book ID
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        // Map feedbacks to feedback responses using the feedback mapper and the connected user ID
        List<FeedbackResponse> feedbackResponses = feedbacks
                .stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
        // Return a page response
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }

}
