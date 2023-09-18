package org.shop.app.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateClientDto {

    @NotNull(message = "Username can't be null")
    @Size(min = 5, message = "Username must be longer than 5")
    private String clientName;

    @NotNull(message = "Password can't be null")
    @Size(min = 5, message = "Password must be longer than 5")
    private String clientPassword;
}
