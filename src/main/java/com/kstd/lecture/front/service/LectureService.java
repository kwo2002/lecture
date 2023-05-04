package com.kstd.lecture.front.service;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import com.kstd.lecture.domain.dto.EmployeeDto;
import com.kstd.lecture.domain.dto.EnrolledLectureDto;
import com.kstd.lecture.domain.dto.EmployeeEnrolledLecturesDto;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.domain.entity.EntityEmployee;
import com.kstd.lecture.domain.entity.EntityEnrolledLectureEmployee;
import com.kstd.lecture.domain.entity.EntityLecture;
import com.kstd.lecture.exception.AlreadyEnrolledLectureException;
import com.kstd.lecture.exception.EmployeeNotFoundException;
import com.kstd.lecture.exception.LectureNotFoundException;
import com.kstd.lecture.front.repository.EmployeeRepository;
import com.kstd.lecture.front.repository.EnrolledLectureEmployeeRepository;
import com.kstd.lecture.front.repository.LectureRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LectureService {

  private final LectureRepository lectureRepository;
  private final EmployeeRepository employeeRepository;
  private final EnrolledLectureEmployeeRepository enrolledLectureEmployeeRepository;
  private final ModelMapper modelMapper;

  public LectureService(LectureRepository lectureRepository, EmployeeRepository employeeRepository,
      EnrolledLectureEmployeeRepository enrolledLectureEmployeeRepository,
      ModelMapper modelMapper) {
    this.lectureRepository = lectureRepository;
    this.employeeRepository = employeeRepository;
    this.enrolledLectureEmployeeRepository = enrolledLectureEmployeeRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional(readOnly = true)
  public List<LectureDto> findAvailableLectures() {
    LocalDate now = LocalDate.now();
    LocalDate afterWeek = now.plusWeeks(1);
    // 강의 시작일이 오늘 이전이면 하루이상 지났으므로 조회할 필요 없다.
    // 오늘로부터 일주일안에 시작하는 강의를 조회한다.
    List<EntityLecture> availableLectures = this.lectureRepository.findByLectureAtBetweenAndUseFlagIsTrueOrderByRegisteredAtDesc(
        now, afterWeek);
    return availableLectures.stream().map(l -> this.modelMapper.map(l, LectureDto.class)).toList();
  }

  @Transactional
  public EnrolledLectureDto saveEnrolledLectureEmployee(Long lectureId, String employeeNo) {
    EntityLecture entityLecture = this.lectureRepository.findByIdAndUseFlagIsTrue(lectureId)
        .orElseThrow(() -> new LectureNotFoundException("존재하지 않는 강의입니다."));

    EntityEmployee entityEmployee =
        this.employeeRepository.findByEmployeeNoAndUseFlagIsTrue(employeeNo)
            .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않는 사번입니다."));

    this.enrolledLectureEmployeeRepository.findByLectureAndEmployeeAndUseFlagIsTrue(entityLecture,
            entityEmployee)
        .ifPresentOrElse(e -> {
          throw new AlreadyEnrolledLectureException("이미 수강신청하셨습니다.");
        }, () -> saveEnrolledLectureEmployee(entityLecture, entityEmployee));

    EnrolledLectureDto enrolledLectureDto = new EnrolledLectureDto();
    enrolledLectureDto.setLecture(this.modelMapper.map(entityLecture, LectureDto.class));
    enrolledLectureDto.setEmployee(this.modelMapper.map(entityEmployee, EmployeeDto.class));

    return enrolledLectureDto;
  }

  private void saveEnrolledLectureEmployee(EntityLecture entityLecture,
      EntityEmployee entityEmployee) {
    entityLecture.increaseCurrentEnrollment();
    this.lectureRepository.save(entityLecture);

    EntityEnrolledLectureEmployee entityEnrolledLectureEmployee = new EntityEnrolledLectureEmployee();
    entityEnrolledLectureEmployee.setLecture(entityLecture);
    entityEnrolledLectureEmployee.setEmployee(entityEmployee);
    entityEnrolledLectureEmployee.setUseFlag(true);
    entityEnrolledLectureEmployee.setRegisteredUserId(entityEmployee.getId());
    this.enrolledLectureEmployeeRepository.save(entityEnrolledLectureEmployee);
  }

  @Transactional(readOnly = true)
  public EmployeeEnrolledLecturesDto findEnrolledLecturesByEmployeeNo(String employeeNo) {
    EntityEmployee entityEmployee =
        this.employeeRepository.findByEmployeeNoAndUseFlagIsTrue(employeeNo)
            .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않는 사번입니다."));

    List<EntityEnrolledLectureEmployee> entityEnrolled =
        this.enrolledLectureEmployeeRepository.findByEmployeeAndUseFlagIsTrue(entityEmployee);

    List<LectureDto> lectureDtos = entityEnrolled
        .stream()
        .map(e -> this.modelMapper.map(e.getLecture(), LectureDto.class))
        .toList();

    EmployeeDto employeeDto = this.modelMapper.map(entityEmployee, EmployeeDto.class);

    EmployeeEnrolledLecturesDto lecturesEmployeeDto = new EmployeeEnrolledLecturesDto();
    lecturesEmployeeDto.setLectures(lectureDtos);
    lecturesEmployeeDto.setEmployee(employeeDto);

    return lecturesEmployeeDto;
  }

  @Transactional
  public EnrolledLectureDto deleteEnrolledLecture(Long lectureId, String employeeNo) {
    EntityLecture entityLecture = this.lectureRepository.findByIdAndUseFlagIsTrue(lectureId)
        .orElseThrow(() -> new LectureNotFoundException("존재하지 않는 강의입니다."));

    EntityEmployee entityEmployee = this.employeeRepository.findByEmployeeNoAndUseFlagIsTrue(
            employeeNo)
        .orElseThrow(() -> new EmployeeNotFoundException("존재하지 않는 사번입니다."));

    EntityEnrolledLectureEmployee entityEnrolled = this.enrolledLectureEmployeeRepository.findByLectureAndEmployeeAndUseFlagIsTrue(
            entityLecture, entityEmployee)
        .orElseThrow(() -> new LectureNotFoundException("신청한 강의가 없습니다."));

    entityLecture.decreaseCurrentEnrollment();
    this.lectureRepository.save(entityLecture);

    entityEnrolled.setUseFlag(false);
    this.enrolledLectureEmployeeRepository.save(entityEnrolled);

    EnrolledLectureDto enrolledLectureDto = new EnrolledLectureDto();
    enrolledLectureDto.setLecture(this.modelMapper.map(entityLecture, LectureDto.class));
    enrolledLectureDto.setEmployee(this.modelMapper.map(entityEmployee, EmployeeDto.class));

    return enrolledLectureDto;
  }

  @Transactional(readOnly = true)
  public List<LectureDto> getFavoriteLectures() {
    LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
    List<EntityEnrolledLectureEmployee> enrolledList = this.enrolledLectureEmployeeRepository.findByRegisteredAtGreaterThanEqualOrderByRegisteredAtDesc(
        threeDaysAgo);

    Map<EntityLecture, Long> enrollCountMap = enrolledList.stream()
        .collect(groupingBy(EntityEnrolledLectureEmployee::getLecture, counting()));

    List<EntityLecture> lectures = this.lectureRepository.findByUseFlagIsTrue();
    lectures.forEach(l -> l.setCurrentEnrollment(enrollCountMap.getOrDefault(l, 0L).intValue()));

    return lectures.stream()
        .sorted(comparing(EntityLecture::getCurrentEnrollment).reversed())
        .map(l -> this.modelMapper.map(l, LectureDto.class))
        .toList();
  }
}
