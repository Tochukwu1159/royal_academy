package examination.teacherAndStudents.objectMapper;

import examination.teacherAndStudents.dto.DormitoryRoomRequest;
import examination.teacherAndStudents.dto.DormitoryRoomResponse;
import examination.teacherAndStudents.dto.DriverRequest;
import examination.teacherAndStudents.dto.DriverResponse;
import examination.teacherAndStudents.entity.DormitoryRooms;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DormitoryRoomsMapper {
    private final ModelMapper modelMapper;
    public DormitoryRooms mapToDomitoryRooms(DormitoryRoomRequest dormitoryRoomRequest) {
        return modelMapper.map(dormitoryRoomRequest, DormitoryRooms.class);
    }

    public DormitoryRoomResponse mapToDomitoryResponse(DormitoryRooms dormitoryRooms) {
        return modelMapper.map(dormitoryRooms, DormitoryRoomResponse.class);
    }
}
