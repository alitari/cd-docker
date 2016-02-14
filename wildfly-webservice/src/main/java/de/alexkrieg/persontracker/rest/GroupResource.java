package de.alexkrieg.persontracker.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import de.alexkrieg.persontracker.domain.AuthAccessElement;
import de.alexkrieg.persontracker.domain.MemberGrouping;
import de.alexkrieg.persontracker.domain.MemberRegistration;
import de.alexkrieg.persontracker.domain.model.Group;
import de.alexkrieg.persontracker.domain.model.Member;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class GroupResource {
    
    @Inject
    MemberGrouping memberGrouping;
    
    @Inject
    MemberRegistration memberRegistration;

 
    @POST
    @Path("group/create/{name}")
    @RolesAllowed({"basic"})
    public GroupTo create( @PathParam("name") String name ) {
        Optional<Group> group = memberGrouping.findGroup(name);
        if (group.isPresent()) {
            return new GroupTo(group.get(), Optional.of("Group already existed."));
        }
        return new GroupTo( memberGrouping.create(name, Optional.empty()),Optional.empty());
    }
    
    @POST
    @Path("group/delete/{name}")
    @RolesAllowed({"basic"})
    public GroupTo delete( @PathParam("name") String name ) {
        Optional<Group> group = memberGrouping.findGroup(name);
        if (!group.isPresent()) {
            return new GroupTo(new Group.Builder(name).build(), Optional.of("Group does not exist."));
        }
        memberGrouping.delete(name);
        return new GroupTo( group.get(),Optional.empty());
    }
    
    @POST
    @Path("group/add/{name}")
    @RolesAllowed({"basic"})
    public GroupTo add( @Context HttpServletRequest request, @PathParam("name") String name, String memberEmail ) {
        Optional<Group> group = memberGrouping.findGroup(name);
        if (!group.isPresent()) {
            return new GroupTo(new Group.Builder(name).build(), Optional.of("Group does not exist."),memberEmail);
        }
        Optional<Member> member = memberRegistration.findByEmail(memberEmail);
        if (!member.isPresent()) {
            return new GroupTo(new Group.Builder(name).build(), Optional.of("Member does not exist."),memberEmail);
        }
        Object authId = request.getHeader(AuthAccessElement.PARAM_AUTH_ID);
        if ( !memberEmail.equals(authId)) {
            return new GroupTo(new Group.Builder(name).build(), Optional.of("You only can add yourself to a group."),memberEmail);            
        }
        
        memberGrouping.add(name, member.get());
        return new GroupTo( group.get(),Optional.empty(),member.get());
    }
    
    @GET
    @Path("group/members/{name}")
    @RolesAllowed({"basic"})
    public GroupTo get(  @PathParam("name") String name) {
        Optional<Group> group = memberGrouping.findGroup(name);
        if (!group.isPresent()) {
            return new GroupTo(new Group.Builder(name).build(), Optional.of("Group does not exist."));
        }
        
        Set<Member> members = group.get().getMembers();
        return new GroupTo( group.get(),Optional.empty(),members);
    }
    
}