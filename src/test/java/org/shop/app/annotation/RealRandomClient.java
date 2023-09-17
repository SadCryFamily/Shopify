package org.shop.app.annotation;

import org.shop.app.entity.ClientRoles;
import org.shop.app.factory.RealClientsSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = RealClientsSecurityContextFactory.class)
public @interface RealRandomClient {

    String clientName() default "basicClient";

    String clientPassword() default "basicPassword";

    ClientRoles role() default ClientRoles.ROLE_USER;

}
