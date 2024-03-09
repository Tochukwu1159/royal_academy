package examination.teacherAndStudents.controller;

import examination.teacherAndStudents.dto.HostelRequest;
import examination.teacherAndStudents.entity.Hostel;
import examination.teacherAndStudents.service.HostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class Controller {
    @Autowired
    private HostelService hostelService;
    @PostMapping("/add")
    public ResponseEntity<Hostel> createHostel(@RequestBody HostelRequest hostel) {
        Hostel createdHostel = hostelService.createHostel(hostel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHostel);
    }


}
