package com.neighborcouponbook.repository;

import com.neighborcouponbook.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    int countByIsDeleted(Boolean isDeleted);
}
