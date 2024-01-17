package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Comment c WHERE c.id = ?1")
    int deleteByIdAndReturnCount(Long commentId);

    List<Comment> findAllByAuthorId(Long authorId, Pageable page);

    List<Comment> findAllByEventId(Long eventId);
}
