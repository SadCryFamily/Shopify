package org.shop.app.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateClientDto {

    private String clientName;

    private String clientPassword;
}
