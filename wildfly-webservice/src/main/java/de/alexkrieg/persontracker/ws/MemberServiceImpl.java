
package de.alexkrieg.persontracker.ws;

import java.util.Optional;

import javax.inject.Inject;
import javax.jws.WebService;

import de.alexkrieg.persontracker.domain.MemberRegistration;
import de.alexkrieg.persontracker.domain.model.Member;
import de.alexkrieg.persontracker.ws.to.AddMemberTo;
import de.alexkrieg.persontracker.ws.to.DeleteMemberTo;
import de.alexkrieg.persontracker.ws.to.MemberTo;

@WebService(serviceName = "MemberService", portName = "MemberService", name = "MemberService", endpointInterface = "de.alexkrieg.persontracker.ws.MemberService", targetNamespace = "http://www.alexkrieg.de/cd-docker/persontracker")
public class MemberServiceImpl implements MemberService {

    @Inject
    MemberRegistration memberRegistration;

    @Override
    public MemberTo addMember(AddMemberTo addMember) {
        MemberTo result = new MemberTo(); 
        try {
            Optional<Member> alreadyRegistered = memberRegistration.findByEmail(addMember.getEmail());
            if (alreadyRegistered.isPresent()) {
                result.setErrorMessage("Member with email=\"" + addMember.getEmail() + "\" already exists");
                return result;
            }
            fillMemberTo(memberRegistration.register(transformMember(addMember)), result);
        } catch (Exception e) {
            result.setErrorMessage("Exception: " + e + " occured");
            return result;
        }
        return result;
    }

    private static Member transformMember(AddMemberTo addMember) {
        return new Member.Builder(addMember.getEmail()).withPassword(addMember.getPassword())
                .build();
    }

    private static void fillMemberTo(Member member, MemberTo memberTo) {
        memberTo.setEmail(member.getEmail());
        memberTo.setId(member.getId());
    }

    @Override
    public MemberTo deleteMember(DeleteMemberTo addMember) {
        MemberTo result = new MemberTo();
        try {
            Optional<Member> memberExists = memberRegistration.findByEmail(addMember.getEmail());
            if (!memberExists.isPresent()) {
                result.setErrorMessage("Member with email=\"" + addMember.getEmail() + "\" does not exist.");
                return result;
            }
            memberRegistration.unregister(memberExists.get().getId());
            fillMemberTo(memberExists.get(), result);
        } catch (Exception e) {
            result.setErrorMessage("Exception: " + e + " occured");
            return result;
        }
        return result;
    }

}
