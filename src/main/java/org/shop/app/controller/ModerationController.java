package org.shop.app.controller;

import org.shop.app.dto.ProvideAuthorityDto;
import org.shop.app.dto.RemoveAuthorityDto;
import org.shop.app.dto.ViewClientModerationDto;
import org.shop.app.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/moderation")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    @GetMapping("/clients")
    @PreAuthorize("hasRole('MODERATOR')")
    public List<ViewClientModerationDto> retrieveAllClientAuthorities() {
        return moderationService.retrieveAllAuthorities();
    }

    @PostMapping("/grant")
    @PreAuthorize("hasRole('MODERATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public String provideAuthoritiesTo(@RequestBody @Valid ProvideAuthorityDto provideAuthorityDto) {
        return moderationService.provideAuthoritiesTo(provideAuthorityDto);
    }

    @PostMapping("/descent")
    @PreAuthorize("hasRole('MODERATOR')")
    public String remoteAuthoritiesOf(@RequestBody @Valid RemoveAuthorityDto removeAuthorityDto) {
        return moderationService.removeAuthoritiesOf(removeAuthorityDto);
    }

}
