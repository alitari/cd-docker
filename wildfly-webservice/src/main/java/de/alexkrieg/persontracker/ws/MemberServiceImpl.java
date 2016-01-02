
package de.alexkrieg.persontracker.ws;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import de.alexkrieg.persontracker.ws.to.AddMemberTo;
import de.alexkrieg.persontracker.ws.to.MemberTo;

@WebService(serviceName = "MemberService", portName = "MemberService", name = "MemberService", endpointInterface = "de.alexkrieg.persontracker.ws.MemberService", targetNamespace = "http://www.alexkrieg.de/cd-docker/persontracker")
public class MemberServiceImpl implements MemberService {

    private static final Logger LOGGER = Logger.getLogger(MemberServiceImpl.class);

    @Override
    public MemberTo addMember(AddMemberTo addMember) {
        LOGGER.info("Member " + addMember.getEmail() + " added!");
        MemberTo member = transformMember(addMember);
        return member;
    }

    private static MemberTo transformMember(AddMemberTo addMember) {
        MemberTo member = new MemberTo();
        member.setId(System.currentTimeMillis());
        member.setEmail(addMember.getEmail());
        return member;
    }

    @Override
    public MemberTo deleteMember(AddMemberTo addMember) {
        LOGGER.info("Member " + addMember + " deleted!");
        return transformMember(addMember);
    }

}
