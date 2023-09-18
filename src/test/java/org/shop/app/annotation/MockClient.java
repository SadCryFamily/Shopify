package org.shop.app.annotation;

import org.shop.app.entity.ClientRoles;
import org.shop.app.factory.MockClientsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = MockClientsSecurityContextFactory.class)
public @interface MockClient {

    String clientName() default "mockClient";

    String clientPassword() default "mockPassword";

    ClientRoles addRole() default ClientRoles.ROLE_USER;

}
