package com.neighborcouponbook.repository;

import com.neighborcouponbook.model.ShopType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopTypeRepository extends JpaRepository<ShopType, Long> {
    int countByIsDeleted(Boolean isDeleted);
}
