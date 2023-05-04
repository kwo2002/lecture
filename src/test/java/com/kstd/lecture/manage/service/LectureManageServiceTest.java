package com.kstd.lecture.manage.service;

import com.kstd.lecture.domain.dto.CreateLectureRequest;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.domain.dto.LectureEnrolledEmployeesDto;
import com.kstd.lecture.domain.entity.EntityEmployee;
import com.kstd.lecture.domain.entity.EntityEnrolledLectureEmployee;
import com.kstd.lecture.domain.entity.EntityLecture;
import com.kstd.lecture.front.repository.EmployeeRepository;
import com.kstd.lecture.front.repository.EnrolledLectureEmployeeRepository;
import com.kstd.lecture.manage.repository.LectureManageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LectureManageServiceTest {
    @Autowired
    private LectureManageService lectureManageService;
    @Autowired
    private LectureManageRepository lectureRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EnrolledLectureEmployeeRepository enrolledLectureEmployeeRepository;
    private EntityLecture availLecture;
    private EntityEmployee alreadyEnrolledEmployee;


    @BeforeEach
    void setUp() {
        LocalDate now = LocalDate.now();
        LocalDate availAt = now.plusDays(3);
        EntityLecture availLecture = new EntityLecture();
        availLecture.setLectureAt(availAt);
        availLecture.setLectureDesc("desc");
        availLecture.setRoom("A-100");
        availLecture.setTeacher("Teacher-11");
        availLecture.setUseFlag(true);
        availLecture.setMaxEnrollment(10);
        availLecture.setRegisteredUserId(100011L);
        this.availLecture = this.lectureRepository.save(availLecture);

        EntityEmployee alreadyEnrolledEmployee = new EntityEmployee();
        alreadyEnrolledEmployee.setUseFlag(true);
        alreadyEnrolledEmployee.setEmployeeNo("A2020");
        alreadyEnrolledEmployee.setRegisteredUserId(1111L);
        this.alreadyEnrolledEmployee = this.employeeRepository.save(alreadyEnrolledEmployee);

        EntityEnrolledLectureEmployee entityEnrolledLectureEmployee = new EntityEnrolledLectureEmployee();
        entityEnrolledLectureEmployee.setEmployeeAndLecture(alreadyEnrolledEmployee, this.availLecture);
        entityEnrolledLectureEmployee.setUseFlag(true);
        entityEnrolledLectureEmployee.setRegisteredUserId(1111L);
        this.enrolledLectureEmployeeRepository.save(entityEnrolledLectureEmployee);

        this.availLecture.increaseCurrentEnrollment();
        this.lectureRepository.save(this.availLecture);
    }

    @Test
    @DisplayName("모든 강의 조회")
    void testGetAllLecture() {
        List<LectureDto> allLecture = this.lectureManageService.findAllLecture();
        assertAll(
                () -> assertTrue(allLecture.size() >= 1),
                () -> assertTrue(allLecture.stream().anyMatch(l -> Objects.equals(l.getId(), availLecture.getId())))
        );
    }

    @Test
    @DisplayName("강의 생성 테스트")
    void testSaveLecture() {
        CreateLectureRequest lectureRequest = new CreateLectureRequest();
        lectureRequest.setLectureAt(LocalDate.now());
        lectureRequest.setLectureDesc("desc");
        lectureRequest.setRoom("room");
        lectureRequest.setTeacher("teacher111");
        lectureRequest.setMaxEnrollment(10);

        LectureDto lecture = this.lectureManageService.createLecture(lectureRequest);
        assertAll(
                () -> assertNotNull(lecture),
                () -> assertEquals(lectureRequest.getTeacher(), lecture.getTeacher())
        );
    }

    @Test
    @DisplayName("강의 상세 및 신청자 목록 조회")
    @Transactional
    void testGetLecture() {
        LectureEnrolledEmployeesDto enrolledEmployeesByLecture = this.lectureManageService.findEnrolledEmployeesByLecture(this.availLecture.getId());
        assertAll(
                () -> assertEquals(enrolledEmployeesByLecture.getLecture().getId(), this.availLecture.getId()),
                () -> assertTrue(enrolledEmployeesByLecture.getEnrolledEmployees().stream().anyMatch(e -> e.getEmployeeNo().equals(this.alreadyEnrolledEmployee.getEmployeeNo())))
        );
    }
}
