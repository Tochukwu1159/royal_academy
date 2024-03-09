package examination.teacherAndStudents.objectMapper;

import examination.teacherAndStudents.dto.OtherStaffRequest;
import examination.teacherAndStudents.dto.OtherStaffResponse;
import examination.teacherAndStudents.entity.User;
import org.springframework.stereotype.Component;

@Component
public class OtherStaffMapper {

    public User mapToOtherStaff(OtherStaffRequest otherStaffRequest) {
        User otherStaff = new User();
        otherStaff.setFirstName(otherStaffRequest.getFirstName());
        otherStaff.setLastName(otherStaffRequest.getLastName());
        otherStaff.setJobDescription(otherStaffRequest.getJobDescription());
        otherStaff.setAddress(otherStaffRequest.getAddress());
        otherStaff.setPhoneNumber(otherStaffRequest.getPhoneNumber());
        // Additional mapping logic if needed
        return otherStaff;
    }

    public OtherStaffResponse mapToOtherStaffResponse(User otherStaff) {
        OtherStaffResponse otherStaffResponse = new OtherStaffResponse();
        otherStaffResponse.setId(otherStaff.getId());
        otherStaffResponse.setFirstName(otherStaff.getFirstName());
        otherStaffResponse.setLastName(otherStaff.getLastName());
        otherStaffResponse.setJobDescription(otherStaff.getJobDescription());
        otherStaffResponse.setAddress(otherStaff.getAddress());
        otherStaffResponse.setPhoneNumber(otherStaff.getPhoneNumber());
        // Additional mapping logic if needed
        return otherStaffResponse;
    }
}

