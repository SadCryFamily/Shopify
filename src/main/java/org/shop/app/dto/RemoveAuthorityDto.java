package org.shop.app.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RemoveAuthorityDto {

    @NotNull(message = "Client name can't be null")
    private String clientName;

}
