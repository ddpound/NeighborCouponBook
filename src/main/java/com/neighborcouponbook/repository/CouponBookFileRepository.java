package com.neighborcouponbook.repository;

import com.neighborcouponbook.model.CouponBookFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponBookFileRepository extends JpaRepository<CouponBookFile, Long> {

    List<CouponBookFile> findByFileGroupId(Long fileGroupId);
}
