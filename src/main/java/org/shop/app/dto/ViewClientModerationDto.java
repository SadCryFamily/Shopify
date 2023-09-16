package org.shop.app.dto;

import lombok.*;
import org.shop.app.entity.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ViewClientModerationDto {

    @NotNull(message = "Username can't be null")
    @Size(min = 5, message = "Username must be longer than 5")
    private String clientName;

    @NotNull(message = "Roles can't be null")
    private Set<Role> roles;

}
