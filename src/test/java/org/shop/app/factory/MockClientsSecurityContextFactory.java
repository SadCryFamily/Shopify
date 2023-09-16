package org.shop.app.factory;

import org.shop.app.annotation.MockClient;
import org.shop.app.entity.ClientRoles;
import org.shop.app.service.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;

public class MockClientsSecurityContextFactory implements WithSecurityContextFactory<MockClient> {

    @Override
    public SecurityContext createSecurityContext(MockClient mockClient) {

        SecurityContext mockSecurityContext = SecurityContextHolder.createEmptyContext();

        UserDetails mockDetails = new UserDetailsImpl(
                mockClient.clientName(),
                mockClient.clientPassword(),
                Boolean.FALSE,
                Set.of(new SimpleGrantedAuthority(ClientRoles.ROLE_USER.extractRoleProperty()))
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                mockDetails,
                mockDetails.getPassword(),
                mockDetails.getAuthorities()
        );

        mockSecurityContext.setAuthentication(authentication);

        return mockSecurityContext;

    }
}
