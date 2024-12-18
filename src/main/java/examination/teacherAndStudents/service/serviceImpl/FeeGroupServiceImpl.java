package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.FeeGroupRequest;
import examination.teacherAndStudents.dto.FeeGroupResponse;
import examination.teacherAndStudents.entity.FeeGroup;
import examination.teacherAndStudents.repository.FeeGroupRepository;
import examination.teacherAndStudents.service.FeeGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeGroupServiceImpl implements FeeGroupService {
    private final FeeGroupRepository feeGroupRepository;

    @Override
        public Page<FeeGroupResponse> getAllFeeGroups(int pageNo, int pageSize, String sortBy){
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<FeeGroup> feeGroups = feeGroupRepository.findAll(paging);
        return feeGroups
                .map(this::mapToFeeGroupResponse);
    }

    @Override
    public FeeGroupResponse getFeeGroupById(Long id) {
        FeeGroup feeGroup = feeGroupRepository.findById(id).orElse(null);
        return feeGroup != null ? mapToFeeGroupResponse(feeGroup) : null;
    }

    @Override
    public FeeGroupResponse createFeeGroup(FeeGroupRequest feeGroupRequest) {
        FeeGroup feeGroup = new FeeGroup();
        mapToFeeGroup(feeGroupRequest);
        FeeGroup savedFeeGroup = feeGroupRepository.save(feeGroup);
        return mapToFeeGroupResponse(savedFeeGroup);
    }

    @Override
    public FeeGroupResponse updateFeeGroup(Long id, FeeGroupRequest feeGroupRequest) {
        FeeGroup existingFeeGroup = feeGroupRepository.findById(id).orElse(null);
        if (existingFeeGroup != null) {
            mapToFeeGroup(feeGroupRequest);
            FeeGroup updatedFeeGroup = feeGroupRepository.save(existingFeeGroup);
            return mapToFeeGroupResponse(updatedFeeGroup);
        }
        return null;
    }

    @Override
    public void deleteFeeGroup(Long id) {
        feeGroupRepository.deleteById(id);
    }

    private FeeGroupResponse mapToFeeGroupResponse(FeeGroup feeGroup) {
        FeeGroupResponse response = new FeeGroupResponse();
        response.setId(feeGroup.getId());
        response.setName(feeGroup.getName());
        response.setAmount(feeGroup.getAmount());
        response.setDescription(feeGroup.getDescription());
        return response;
    }

    private FeeGroup mapToFeeGroup(FeeGroupRequest feeGroupRequest) {
        FeeGroup response = new FeeGroup();
        response.setName(feeGroupRequest.getName());
        response.setAmount(feeGroupRequest.getAmount());
        response.setDescription(feeGroupRequest.getDescription());
        return response;
    }
}

