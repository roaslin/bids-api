package com.witbooking.api.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "item")
public class ItemEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Setter
    protected int id;

    @OneToMany(mappedBy = "item")
    private List<BidEntity> bids;
}
