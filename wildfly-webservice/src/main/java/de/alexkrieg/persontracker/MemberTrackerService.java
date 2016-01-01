
package de.alexkrieg.persontracker;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import de.alexkrieg.persontracker.to.AddMemberTo;
import de.alexkrieg.persontracker.to.MemberTo;

@WebService(targetNamespace = "http://www.alexkrieg.de/cd-docker/membertracker")
public interface MemberTrackerService {

    @WebMethod
    @WebResult(name = "AddedMember")
    public MemberTo addMember(@WebParam(name = "Member") AddMemberTo addMember);

    @WebMethod
    @WebResult(name = "DeletedMember")
    public MemberTo deleteMember(@WebParam(name = "Member") AddMemberTo member);

}
