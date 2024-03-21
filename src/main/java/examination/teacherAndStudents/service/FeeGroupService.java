package examination.teacherAndStudents.service;

import examination.teacherAndStudents.dto.FeeGroupRequest;
import examination.teacherAndStudents.dto.FeeGroupResponse;
import org.springframework.data.domain.Page;

public interface FeeGroupService {
    Page<FeeGroupResponse> getAllFeeGroups(int pageNo, int pageSize, String sortBy);
    FeeGroupResponse getFeeGroupById(Long id);
    FeeGroupResponse createFeeGroup(FeeGroupRequest feeGroupRequest);
    FeeGroupResponse updateFeeGroup(Long id, FeeGroupRequest feeGroupRequest);
    void deleteFeeGroup(Long id);

}
