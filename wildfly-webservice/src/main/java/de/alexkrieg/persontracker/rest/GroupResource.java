package de.alexkrieg.persontracker.rest;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.alexkrieg.persontracker.domain.MemberGrouping;
import de.alexkrieg.persontracker.domain.model.Group;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class GroupResource {
    
    @Inject
    MemberGrouping memberGrouping;
 
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

    
    
}