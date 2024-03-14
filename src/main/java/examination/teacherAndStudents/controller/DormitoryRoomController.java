package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.ClassCategoryResponse;
        import examination.teacherAndStudents.dto.DormitoryRoomRequest;
        import examination.teacherAndStudents.dto.DormitoryRoomResponse;
        import examination.teacherAndStudents.service.DormitoryRoomService;
        import examination.teacherAndStudents.utils.AccountUtils;
        import lombok.RequiredArgsConstructor;
        import org.springframework.data.domain.Page;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;
        import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dormitory_rooms")
@RequiredArgsConstructor
public class DormitoryRoomController {

    private final DormitoryRoomService dormitoryService;

    @GetMapping
        public ResponseEntity<Page<DormitoryRoomResponse>> getAllDormitorys(@RequestParam(defaultValue = AccountUtils.PAGENO) Integer pageNo,
                @RequestParam(defaultValue = AccountUtils.PAGESIZE) Integer pageSize,
                @RequestParam(defaultValue = "id") String sortBy) {
        Page<DormitoryRoomResponse> dormitories = dormitoryService.getAllDormitoryRooms(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(dormitories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DormitoryRoomResponse> getDormitoryRoomById(@PathVariable Long id) {
        Optional<DormitoryRoomResponse> dormitory = dormitoryService.getDormitoryRoomById(id);
        return ResponseEntity.ok(dormitory.get());
    }

    @PostMapping("/add")
    public ResponseEntity<DormitoryRoomResponse> createDormitoryRoom(@RequestBody DormitoryRoomRequest dormitoryRoomRequest) {
        DormitoryRoomResponse createdDormitory = dormitoryService.createDormitoryRoom(dormitoryRoomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDormitory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DormitoryRoomResponse> updateDormitoryRoom(@PathVariable Long id, @RequestBody DormitoryRoomRequest updatedDormitory) {
        DormitoryRoomResponse dormitory = dormitoryService.updateDormitoryRoom(id, updatedDormitory);
        return ResponseEntity.ok(dormitory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDormitoryRoom(@PathVariable Long id) {
        dormitoryService.deleteDormitoryRoom(id);
        return ResponseEntity.ok("Dormitory deleted successfully");
    }

//    @PostMapping("/addStudent")
//    public ResponseEntity<String> addStudentToDormitory(@RequestParam Long studentId, @RequestParam Long dormitoryId) {
//        dormitoryService.addStudentToDormitory(studentId, dormitoryId);
//        return ResponseEntity.ok("Student added to dormitory successfully");
//    }
//
//
//    @GetMapping("/available")
//    public ResponseEntity<List<DormitoryRoomResponse>> getAllAvailableDormitoryRooms() {
//        try {
//            List<Dormitory> availableDormitories = dormitoryService.getAllAvailableDormitoryRooms();
//            return new ResponseEntity<>(availableDormitories, HttpStatus.OK);
//        } catch (CustomNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Return unauthorized status for non-admin users
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @PostMapping("/pay")
//    public ResponseEntity<String> payForDormitory(@RequestParam Long dormitoryId) {
//        try {
//            dormitoryService.payForDormitory(dormitoryId);
//            return ResponseEntity.ok("Payment successful");
//        } catch (InsufficientBalanceException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
//        } catch (NotFoundException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No available beds");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment");
//        }
//    }

}