
package de.alexkrieg.persontracker;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import de.alexkrieg.persontracker.to.AddMemberTo;
import de.alexkrieg.persontracker.to.MemberTo;

@WebService(serviceName = "MemberTrackerService", portName = "MemberTracker", name = "MemberTracker", endpointInterface = "de.alexkrieg.persontracker.MemberTrackerService", targetNamespace = "http://www.alexkrieg.de/cd-docker/membertracker")
public class MemberTrackerServiceImpl implements MemberTrackerService {

	private static final Logger LOGGER = Logger.getLogger(MemberTrackerServiceImpl.class);

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
