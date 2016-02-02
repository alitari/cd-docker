package de.alexkrieg.persontracker.rest;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.alexkrieg.persontracker.domain.AuthAccessElement;
import de.alexkrieg.persontracker.domain.AuthLoginElement;
import de.alexkrieg.persontracker.domain.MemberAuthorization;
import de.alexkrieg.persontracker.domain.model.Member;

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

    @POST
    @Path("logout")
    @RolesAllowed({"basic"})
    public AuthAccessElement logout(@Context HttpServletRequest request, String email) {
        Optional<Member> member = memberAuthorization.findMemberWithEmail(email);
        if (!member.isPresent()) {
            return new AuthAccessElement(email, null, null, "member does not exist");
        }
        if (member.get().getAuthToken() == null) {
            return new AuthAccessElement(email, null, null, "member is not logged in");
        }

        memberAuthorization.logout(email);

        request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_ID, null);
        request.getSession().setAttribute(AuthAccessElement.PARAM_AUTH_TOKEN, null);
        
        return new AuthAccessElement(email, null, null, "OK");
    }
}