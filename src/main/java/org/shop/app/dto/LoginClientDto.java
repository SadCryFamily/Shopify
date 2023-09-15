package org.shop.app.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginClientDto {

    private String clientName;

    private String clientPassword;
}
