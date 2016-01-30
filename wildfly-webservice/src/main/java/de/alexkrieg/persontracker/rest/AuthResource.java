package de.alexkrieg.persontracker.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.alexkrieg.persontracker.domain.AuthAccessElement;
import de.alexkrieg.persontracker.domain.AuthLoginElement;
import de.alexkrieg.persontracker.domain.MemberAuthorization;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class AuthResource {
 
    @Inject
    MemberAuthorization memberAuthorization;
 
    @POST
    @Path("login")
    public AuthAccessElement login(@Context HttpServletRequest request, AuthLoginElement loginElement) {
        AuthAccessElement accessElement = memberAuthorization.login(loginElement);
        if (accessElement != null) {
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_ID, accessElement.getAuthId());
            request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_TOKEN, accessElement.getAuthToken());
        }
        return accessElement;
    }
}