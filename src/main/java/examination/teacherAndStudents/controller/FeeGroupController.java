package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.FeeGroupRequest;
import examination.teacherAndStudents.dto.FeeGroupResponse;
import examination.teacherAndStudents.service.FeeGroupService;
import examination.teacherAndStudents.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feegroups")
@RequiredArgsConstructor
public class FeeGroupController {

    private final FeeGroupService feeGroupService;

    @GetMapping("all")

        public ResponseEntity<Page<FeeGroupResponse>> getAllFeeGroups(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<FeeGroupResponse> feeGroups = feeGroupService.getAllFeeGroups(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(feeGroups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeeGroupResponse> getFeeGroupById(@PathVariable("id") Long id) {
        FeeGroupResponse feeGroup = feeGroupService.getFeeGroupById(id);
        if (feeGroup != null) {
            return new ResponseEntity<>(feeGroup, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<FeeGroupResponse> createFeeGroup(@RequestBody FeeGroupRequest feeGroupRequest) {
        FeeGroupResponse createdFeeGroup = feeGroupService.createFeeGroup(feeGroupRequest);
        return new ResponseEntity<>(createdFeeGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeeGroupResponse> updateFeeGroup(@PathVariable("id") Long id, @RequestBody FeeGroupRequest feeGroupRequest) {
        FeeGroupResponse updatedFeeGroup = feeGroupService.updateFeeGroup(id, feeGroupRequest);
        if (updatedFeeGroup != null) {
            return new ResponseEntity<>(updatedFeeGroup, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeGroup(@PathVariable("id") Long id) {
        feeGroupService.deleteFeeGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
