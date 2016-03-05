package com.witbooking.api.entities;

import com.witbooking.api.persistence.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
    private LocalDateTime expireDate;// 10 minutes valid

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
