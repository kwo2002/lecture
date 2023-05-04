package testsource.lecture;

import com.kstd.lecture.domain.dto.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class LectureSource {
    private static LectureDto getLectureDto() {
        LectureDto lectureDto = new LectureDto();
        lectureDto.setId(1L);
        lectureDto.setLectureAt(LocalDate.now());
        lectureDto.setLectureDesc("강의내용1");
        lectureDto.setRoom("ROOM1");
        lectureDto.setMaxEnrollment(20);
        lectureDto.setCurrentEnrollment(0);
        lectureDto.setTeacher("강사");
        return lectureDto;
    }

    static Stream<LectureDto> lectureDto() {
        LectureDto lectureDto = getLectureDto();
        return Stream.of(lectureDto);
    }

    static Stream<LectureEnrolledEmployeesDto> enrolledEmployeesDto() {
        LectureEnrolledEmployeesDto enrolledEmployeesDto = new LectureEnrolledEmployeesDto();
        LectureDto lectureDto = getLectureDto();
        enrolledEmployeesDto.setLecture(lectureDto);

        EnrolledEmployeeDto enrolledEmployeeDto = new EnrolledEmployeeDto();
        enrolledEmployeeDto.setEmployeeId(1L);
        enrolledEmployeeDto.setEmployeeNo("A1010");
        enrolledEmployeeDto.setRegisteredAt(LocalDateTime.now());
        enrolledEmployeeDto.setUseFlag(true);
        enrolledEmployeesDto.setEnrolledEmployees(List.of(enrolledEmployeeDto));

        return Stream.of(enrolledEmployeesDto);
    }

    static Stream<EnrolledLectureDto> enrolledLectureDto() {
        EnrolledLectureDto enrolledLectureDto = new EnrolledLectureDto();
        LectureDto lectureDto = getLectureDto();

        enrolledLectureDto.setLecture(lectureDto);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(1L);
        employeeDto.setEmployeeNo("A1010");

        enrolledLectureDto.setEmployee(employeeDto);

        return Stream.of(enrolledLectureDto);
    }

    static Stream<EmployeeEnrolledLecturesDto> enrolledLecturesDto() {
        EmployeeEnrolledLecturesDto enrolledLecturesDto = new EmployeeEnrolledLecturesDto();
        LectureDto lectureDto = getLectureDto();
        enrolledLecturesDto.setLectures(List.of(lectureDto));

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(1L);
        employeeDto.setEmployeeNo("A1010");

        enrolledLecturesDto.setEmployee(employeeDto);

        return Stream.of(enrolledLecturesDto);
    }
}
