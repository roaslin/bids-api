package com.witbooking.api.repositories;

import com.witbooking.api.entities.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<BidEntity, Integer> {
}
