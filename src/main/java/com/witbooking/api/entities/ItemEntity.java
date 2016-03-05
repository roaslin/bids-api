package com.witbooking.api.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "item")
public class ItemEntity  implements Serializable {

    @Id
    @NotNull
    @Setter
    protected int id;

    @OneToMany(mappedBy = "item")
    private Set<BidEntity> bidEntities;
}
