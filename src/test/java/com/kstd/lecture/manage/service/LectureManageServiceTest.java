package com.kstd.lecture.manage.service;

import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.domain.dto.CreateLectureRequest;
import com.kstd.lecture.domain.entity.EntityEmployee;
import com.kstd.lecture.domain.entity.EntityLecture;
import com.kstd.lecture.domain.entity.EntityEnrolledLectureEmployee;
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
  private LectureManageService lectureService;
  @Autowired
  private LectureManageRepository lectureRepository;


  private EntityLecture entityLecture;
  private EntityEnrolledLectureEmployee entityEnrolledLectureEmployee;


  @BeforeEach
  void setUp() {
    EntityLecture entityLecture = new EntityLecture();
    entityLecture.setLectureAt(LocalDate.now());
    entityLecture.setLectureDesc("desc");
    entityLecture.setRoom("room");
    entityLecture.setTeacher("teacher");
    entityLecture.setUseFlag(true);
    entityLecture.setMaxEnrollment(10);
    entityLecture.setRegisteredUserId(100011L);
    this.entityLecture = this.lectureRepository.save(entityLecture);

    EntityEmployee entityEmployee = new EntityEmployee();
    entityEmployee.setUseFlag(true);
    entityEmployee.setEmployeeNo("A1010");

//    EntityEnrolledLectureEmployee enrolledLectureEmployee = new EntityEnrolledLectureEmployee();
//    enrolledLectureEmployee.setLecture(this.entityLecture);
//    enrolledLectureEmployee.setUseFlag(true);
//    enrolledLectureEmployee.setEmployee();
  }

  @Test
  @DisplayName("모든 강의 조회")
  void testGetAllLecture() {
    List<LectureDto> allLecture = this.lectureService.findAllLecture();
    assertAll(
            () -> assertTrue(allLecture.size() >= 1),
            () -> assertTrue(allLecture.stream().anyMatch(l -> Objects.equals(l.getId(), entityLecture.getId())))
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

    LectureDto lecture = this.lectureService.createLecture(lectureRequest);
    assertAll(
            () -> assertNotNull(lecture),
            () -> assertEquals(lectureRequest.getTeacher(), lecture.getTeacher())
    );
  }

  @Test
  @DisplayName("강의 상세 및 신청자 목록 조회")
  void testGetLecture() {

  }
}
