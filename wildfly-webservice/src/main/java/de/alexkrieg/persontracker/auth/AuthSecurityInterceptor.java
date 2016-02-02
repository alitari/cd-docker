package de.alexkrieg.persontracker.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import de.alexkrieg.persontracker.domain.AuthAccessElement;
import de.alexkrieg.persontracker.domain.MemberAuthorization;

@Provider
public class AuthSecurityInterceptor implements ContainerRequestFilter {

    // 401 - Access denied
    private static final Response ACCESS_UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("Not authorized.").build();

    @Inject
    MemberAuthorization memberAuthorization;

    @Context
    private HttpServletRequest request;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Get AuthId and AuthToken from HTTP-Header.
        String authId = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_ID);
        String authToken = requestContext.getHeaderString(AuthAccessElement.PARAM_AUTH_TOKEN);

        // Get method invoked.
        Method methodInvoked = resourceInfo.getResourceMethod();

        if (methodInvoked.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAllowedAnnotation = methodInvoked.getAnnotation(RolesAllowed.class);
            Set<String> rolesAllowed = new HashSet<>(Arrays.asList(rolesAllowedAnnotation.value()));

            if (!memberAuthorization.isAuthorized(new AuthAccessElement(authId, authToken, null))) {
                requestContext.abortWith(ACCESS_UNAUTHORIZED);
            }
        }
    }
}

// 6cd2ba7f-dad8-44e6-90d2-89c75e61a045
// alex2@mail.com