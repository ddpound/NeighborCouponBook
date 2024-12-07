package com.neighborcouponbook.repository;

import com.neighborcouponbook.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    int countByIsDeleted(Boolean isDeleted);
}
