package examination.teacherAndStudents.service.serviceImpl;

import examination.teacherAndStudents.dto.FeeGroupResponse;
import examination.teacherAndStudents.dto.LibraryMemberResponse;
import examination.teacherAndStudents.dto.LibraryMembershipRequest;
import examination.teacherAndStudents.entity.LibraryMembership;
import examination.teacherAndStudents.entity.SubClass;
import examination.teacherAndStudents.entity.User;
import examination.teacherAndStudents.error_handler.CustomInternalServerException;
import examination.teacherAndStudents.error_handler.EntityNotFoundException;
import examination.teacherAndStudents.error_handler.ResourceNotFoundException;
import examination.teacherAndStudents.repository.LibraryMemberRepository;
import examination.teacherAndStudents.repository.SubClassRepository;
import examination.teacherAndStudents.repository.UserRepository;
import examination.teacherAndStudents.service.LibraryMemberService;
import examination.teacherAndStudents.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibraryMemberServiceImpl implements LibraryMemberService {

    private final LibraryMemberRepository
            libraryMemberRepository;
    private final UserRepository userRepository;
    private final SubClassRepository subClassRepository;

    @Override
    public LibraryMemberResponse createLibraryMember(LibraryMembershipRequest request) {
        try {
            Optional<User> student = userRepository.findByUniqueRegistrationNumber(request.getUserUniqueRegistrationNumber());
            if (!student.isPresent()) {
                throw new ResourceNotFoundException("User not found with unique registration number: " + request.getUserUniqueRegistrationNumber());
            }
            String studentName = student.get().getFirstName() + student.get().getLastName();

            Optional<SubClass> studentClass = subClassRepository.findById(request.getUserClassId());
            if (!studentClass.isPresent()) {
                throw new ResourceNotFoundException("User class not found with ID: " + request.getUserClassId());
            }
            String studentClassName = studentClass.get().getSubClassName();

            String membershipId = AccountUtils.generateLibraryId();
            LibraryMembership libraryMember = new LibraryMembership();
            libraryMember.setUserClass(studentClassName);
            libraryMember.setUserName(studentName);
            libraryMember.setUserRole(request.getUserRole());
            libraryMember.setMemberId(membershipId);

            LibraryMembership savedMember = libraryMemberRepository.save(libraryMember);
            return mapToLibraryMemberResponse(savedMember);
        } catch (Exception e) {
            throw new CustomInternalServerException("Error creating library member: " + e.getMessage());
        }
    }

    @Override
    public LibraryMemberResponse updateLibraryMember(Long memberId, LibraryMembershipRequest request) {
        try {
            Optional<LibraryMembership> existingMemberOpt = libraryMemberRepository.findById(memberId);
            if (!existingMemberOpt.isPresent()) {
                throw new ResourceNotFoundException("Library membership not found with ID: " + memberId);
            }
            LibraryMembership existingMember = existingMemberOpt.get();
            Optional<SubClass> studentClass = subClassRepository.findById(request.getUserClassId());
            if (!studentClass.isPresent()) {
                throw new ResourceNotFoundException("User class not found with ID: " + request.getUserClassId());
            }
            String studentClassName = studentClass.get().getSubClassName();

            existingMember.setUserRole(request.getUserRole());
            existingMember.setUserClass(studentClassName);
            LibraryMembership updatedMember = libraryMemberRepository.save(existingMember);

            return mapToLibraryMemberResponse(updatedMember);
        } catch (Exception e) {
            // Handle any unexpected exceptions
            throw new CustomInternalServerException("Error updating library member: " + e.getMessage());
        }
    }

    @Override
    public LibraryMemberResponse findById(Long id) {
        LibraryMembership libraryMember = libraryMemberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library member not found with id: " + id));
        return mapToLibraryMemberResponse(libraryMember);
    }

    @Override

        public Page<LibraryMemberResponse> findAll(int pageNo, int pageSize, String sortBy){
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<LibraryMembership> libraryMembers = libraryMemberRepository.findAll(paging);
        return libraryMembers
                .map(this::mapToLibraryMemberResponse);

    }

    @Override
    public void deleteLibraryMember(Long id) {
        libraryMemberRepository.deleteById(id);
    }


    private LibraryMemberResponse mapToLibraryMemberResponse(LibraryMembership libraryMember) {
        LibraryMemberResponse response = new LibraryMemberResponse();
        response.setId(libraryMember.getId());
        response.setUserRole(libraryMember.getUserRole());
        response.setUserClass(libraryMember.getUserClass());
        response.setUserName(libraryMember.getUserName());
        response.setMemberId(libraryMember.getMemberId());
        return response;
    }
}

