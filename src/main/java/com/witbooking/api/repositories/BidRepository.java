package com.witbooking.api.repositories;

import com.witbooking.api.entities.BidEntity;
import com.witbooking.api.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BidRepository extends JpaRepository<BidEntity, Integer> {
    @Query(value = "SELECT B1.* FROM bid B1 " +
            "LEFT JOIN bid B2 ON (B1.USER_ID = B2.USER_ID AND B1.amount < B2.amount) " +
            "WHERE B1.item_id = ?1 AND B2.amount IS NULL " +
            "ORDER BY B1.amount DESC", nativeQuery = true)
    List<BidEntity> findByItem(ItemEntity item);
}
