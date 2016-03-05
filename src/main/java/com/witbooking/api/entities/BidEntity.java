package com.witbooking.api.entities;

import com.witbooking.api.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "bid")
public class BidEntity extends BaseEntity {

    private BigDecimal amount;

    @ManyToOne
    private ItemEntity item;

    @ManyToOne
    private UserEntity user;
}
