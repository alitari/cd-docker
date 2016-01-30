
package de.alexkrieg.persontracker.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import de.alexkrieg.persontracker.ws.to.AddMemberTo;
import de.alexkrieg.persontracker.ws.to.DeleteMemberTo;
import de.alexkrieg.persontracker.ws.to.MemberTo;

@WebService(targetNamespace = "http://www.alexkrieg.de/cd-docker/persontracker")
public interface MemberService {

    @WebMethod
    @WebResult(name = "AddedMember")
    public MemberTo addMember(@WebParam(name = "Member") AddMemberTo addMember);

    @WebMethod
    @WebResult(name = "DeletedMember")
    public MemberTo deleteMember(@WebParam(name = "Member") DeleteMemberTo member);

}
