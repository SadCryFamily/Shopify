package org.shop.app.service;

import org.shop.app.dto.ProvideAuthorityDto;
import org.shop.app.dto.RemoveAuthorityDto;
import org.shop.app.dto.ViewClientModerationDto;

import java.util.List;

public interface ModerationService {

    List<ViewClientModerationDto> retrieveAllAuthorities();

    String provideAuthoritiesTo(ProvideAuthorityDto provideAuthorityDto);

    String removeAuthoritiesOf(RemoveAuthorityDto removeAuthorityDto);

}
