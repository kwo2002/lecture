package com.kstd.lecture.front.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kstd.lecture.domain.dto.EnrolledLectureDto;
import com.kstd.lecture.domain.dto.EmployeeEnrolledLecturesDto;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.domain.entity.EntityEmployee;
import com.kstd.lecture.domain.entity.EntityEnrolledLectureEmployee;
import com.kstd.lecture.domain.entity.EntityLecture;
import com.kstd.lecture.exception.AlreadyEnrolledLectureException;
import com.kstd.lecture.exception.EmployeeNotFoundException;
import com.kstd.lecture.exception.EnrollLimitExceedException;
import com.kstd.lecture.exception.LectureNotFoundException;
import com.kstd.lecture.front.repository.EmployeeRepository;
import com.kstd.lecture.front.repository.EnrolledLectureEmployeeRepository;
import com.kstd.lecture.front.repository.LectureRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureServiceTest {

  @Autowired
  private LectureService lectureService;
  @Autowired
  private LectureRepository lectureRepository;
  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private EnrolledLectureEmployeeRepository lectureEmployeeRepository;

  private EntityLecture availLecture;
  private EntityLecture notAvailLecture;
  private EntityEmployee entityEmployee;
  private EntityEmployee alreadyEnrolledEmployee;
  private EntityEnrolledLectureEmployee entityEnrolledLectureEmployee;
  private final String NOT_EXIST_EMPLOYEE_NO = "Z9900";
  private final Long NOT_EXIST_LECTURE_ID = 9999L;

  // 테스트데이터 초기화
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

    LocalDate notAvailAt = now.plusDays(10);
    EntityLecture notAvailLecture = new EntityLecture();
    notAvailLecture.setLectureAt(notAvailAt);
    notAvailLecture.setLectureDesc("desc");
    notAvailLecture.setRoom("B-100");
    notAvailLecture.setTeacher("Teacher-22");
    notAvailLecture.setUseFlag(true);
    notAvailLecture.setMaxEnrollment(0);
    notAvailLecture.setRegisteredUserId(100011L);
    this.notAvailLecture = this.lectureRepository.save(notAvailLecture);

    EntityEmployee entityEmployee = new EntityEmployee();
    entityEmployee.setUseFlag(true);
    entityEmployee.setEmployeeNo("A1234");
    entityEmployee.setRegisteredUserId(1111L);
    this.entityEmployee = this.employeeRepository.save(entityEmployee);

    EntityEmployee alreadyEnrolledEmployee = new EntityEmployee();
    alreadyEnrolledEmployee.setUseFlag(true);
    alreadyEnrolledEmployee.setEmployeeNo("A2020");
    alreadyEnrolledEmployee.setRegisteredUserId(1111L);
    this.alreadyEnrolledEmployee = this.employeeRepository.save(alreadyEnrolledEmployee);

    EntityEnrolledLectureEmployee entityEnrolledLectureEmployee = new EntityEnrolledLectureEmployee();
    entityEnrolledLectureEmployee.setLecture(this.availLecture);
    entityEnrolledLectureEmployee.setEmployee(this.alreadyEnrolledEmployee);
    entityEnrolledLectureEmployee.setUseFlag(true);
    entityEnrolledLectureEmployee.setRegisteredUserId(1111L);
    this.entityEnrolledLectureEmployee = this.lectureEmployeeRepository.save(
        entityEnrolledLectureEmployee);

    this.availLecture.increaseCurrentEnrollment();
    this.lectureRepository.save(this.availLecture);
  }

  @Test
  @DisplayName("신청 가능한 강연 목록 (일주일 내에 시작하는 강연)")
  void testGetAvalialbleLectures() {
    List<LectureDto> availableLectures = this.lectureService.findAvailableLectures();
    assertAll(
        // 3일 후에 시작하는 강의. 일주일내 시작하는 강의이므로 조회돼야한다.
        () -> assertTrue(availableLectures.stream()
            .anyMatch(l -> Objects.equals(l.getId(), availLecture.getId()))),

        // 10일 후에 시작하는 강의. 아직 일주일 전이 아니므로 조회되지 않아야한다.
        () -> assertTrue(availableLectures.stream()
            .noneMatch(l -> Objects.equals(l.getId(), notAvailLecture.getId())))
    );
  }

  @Test
  @DisplayName("강의 신청 성공")
  void testPostEnrollLecture() {
    int currentEnrollment = this.availLecture.getCurrentEnrollment();
    EnrolledLectureDto enrolledLectureDto = this.lectureService.saveEnrolledLectureEmployee(
        this.availLecture.getId(), this.entityEmployee.getEmployeeNo());
    assertAll(
        () -> assertNotNull(enrolledLectureDto),
        () -> assertEquals(enrolledLectureDto.getLecture().getId(), this.availLecture.getId()),
        () -> assertEquals(enrolledLectureDto.getEmployee().getEmployeeNo(),
            this.entityEmployee.getEmployeeNo()),
        () -> assertEquals(enrolledLectureDto.getLecture().getCurrentEnrollment(),
            currentEnrollment + 1)
    );
  }

  @Test
  @DisplayName("강의 신청 실패 - 존재하지 않는 강의번호로 신청")
  void fail_notFoundLecture() {
    assertThrows(LectureNotFoundException.class,
        () -> this.lectureService.saveEnrolledLectureEmployee(NOT_EXIST_LECTURE_ID,
            this.entityEmployee.getEmployeeNo()));
  }

  @Test
  @DisplayName("강의 신청 실패 - 신청인원 초과")
  void fail_enroll_limit_exceed() {
    assertThrows(EnrollLimitExceedException.class,
        () -> this.lectureService.saveEnrolledLectureEmployee(this.notAvailLecture.getId(),
            this.entityEmployee.getEmployeeNo()));
  }

  @Test
  @DisplayName("강의 신청 실패 - 존재하지 않는 사번으로 신청")
  void fail_notFoundEmployeeNo() {

    assertThrows(EmployeeNotFoundException.class,
        () -> this.lectureService.saveEnrolledLectureEmployee(this.availLecture.getId(), NOT_EXIST_EMPLOYEE_NO));
  }

  @Test
  @DisplayName("강의 신청 실패 - 이미 동일한 사번으로 동일한 강의를 신청")
  void fail_alreadyEnrolled() {
    assertThrows(AlreadyEnrolledLectureException.class,
        () -> this.lectureService.saveEnrolledLectureEmployee(this.availLecture.getId(),
            this.alreadyEnrolledEmployee.getEmployeeNo()));
  }

  @Test
  @DisplayName("사번으로 신청한 강연 목록 조회 ")
  void testFindEnrolledLecturesByEmployeeNo() {
    EmployeeEnrolledLecturesDto enrolledLecturesDto = this.lectureService.findEnrolledLecturesByEmployeeNo(
        this.alreadyEnrolledEmployee.getEmployeeNo());
    assertAll(
        () -> assertTrue(enrolledLecturesDto.getLectures().stream()
            .anyMatch(l -> l.getId().equals(availLecture.getId()))),
        () -> assertEquals(enrolledLecturesDto.getEmployee().getEmployeeNo(),
            this.alreadyEnrolledEmployee.getEmployeeNo())
    );
  }

  @Test
  @DisplayName("사번으로 신청한 강연 목록 조회 실패 - 존재하지 않는 사번으로 조회한 경우")
  void fail_findEnrolledLecturesByEmployeeNo() {
    assertThrows(EmployeeNotFoundException.class,
        () -> this.lectureService.findEnrolledLecturesByEmployeeNo(NOT_EXIST_EMPLOYEE_NO));
  }

  @Test
  @DisplayName("신청한 강연 취소")
  void test_deleteEnroll() {
    assertAll(
        () -> {
          // 최초상태 - 현재 강연신청 수 1
          assertEquals(this.availLecture.getCurrentEnrollment(), 1);
          // 최초상태 useFlag True
          assertTrue(this.entityEnrolledLectureEmployee.isUseFlag());

          // 강연 취소
          EnrolledLectureDto enrolledLectureDto = this.lectureService.deleteEnrolledLecture(
              this.availLecture.getId(), this.alreadyEnrolledEmployee.getEmployeeNo());

          assertAll(
              () -> {
                assertNotNull(enrolledLectureDto);
                // 강연 취소 후 강연신청 수 0
                assertEquals(this.availLecture.getCurrentEnrollment(), 0);
                // 강연 취소 후 강연신청 useFlag False
                assertFalse(this.entityEnrolledLectureEmployee.isUseFlag());
              }
          );
        }
    );
  }

  @Test
  @DisplayName("최근 3일간 신청횟수 많은 강연 인기순 정렬")
  void testFavoriteLectures() {
    List<LectureDto> favoriteLectures = this.lectureService.getFavoriteLectures();
    int currentEnrolledCount = Integer.MAX_VALUE;
    for (LectureDto favoriteLecture : favoriteLectures) {
      assertTrue(favoriteLecture.getCurrentEnrollment() <= currentEnrolledCount);
      currentEnrolledCount = favoriteLecture.getCurrentEnrollment();
    }

  }
}
