package com.witbooking.api.entities;

import com.witbooking.api.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "login")
public class LoginEntity extends BaseEntity {

    @NotNull
    private String sessionKey;

    @NotNull
    private LocalDateTime validUntil;// 10 minutes valid

    @NotNull
    @OneToOne
    private UserEntity user;
}
