package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Compilation c WHERE c.id = ?1")
    int deleteByIdAndReturnCount(Integer compId);

    @Query("SELECT c FROM Compilation c WHERE c.pinned = :pinned OR :pinned IS NULL")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
