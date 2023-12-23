package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> getUsersByIdIn(Collection<Long> id);

    Page<User> findAll(Pageable page);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM User u WHERE u.id = ?1")
    int deleteByIdAndReturnCount(Long userId);
}
