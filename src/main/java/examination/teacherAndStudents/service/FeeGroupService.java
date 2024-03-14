package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.FeeGroupRequest;
import examination.teacherAndStudents.dto.FeeGroupResponse;
import examination.teacherAndStudents.entity.Feedback;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeeGroupService {
    Page<FeeGroupResponse> getAllFeeGroups(int pageNo, int pageSize, String sortBy);
    FeeGroupResponse getFeeGroupById(Long id);
    FeeGroupResponse createFeeGroup(FeeGroupRequest feeGroupRequest);
    FeeGroupResponse updateFeeGroup(Long id, FeeGroupRequest feeGroupRequest);
    void deleteFeeGroup(Long id);

}
