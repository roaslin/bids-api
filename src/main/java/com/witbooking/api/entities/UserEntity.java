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
@Entity(name = "user")
public class UserEntity implements Serializable {

    @Id
    @NotNull
    protected int id;

    @OneToMany(mappedBy = "user")
    private List<BidEntity> bids;

    @OneToMany(mappedBy = "user")
    private List<LoginEntity> login;
}
