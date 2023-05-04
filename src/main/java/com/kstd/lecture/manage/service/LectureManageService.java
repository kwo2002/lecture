package com.kstd.lecture.manage.service;

import com.kstd.lecture.domain.dto.CreateLectureRequest;
import com.kstd.lecture.domain.dto.EnrolledEmployeeDto;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.domain.dto.LectureEnrolledEmployeesDto;
import com.kstd.lecture.domain.entity.EntityEmployee;
import com.kstd.lecture.domain.entity.EntityEnrolledLectureEmployee;
import com.kstd.lecture.domain.entity.EntityLecture;
import com.kstd.lecture.exception.LectureNotFoundException;
import com.kstd.lecture.manage.repository.LectureManageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LectureManageService {

  private final LectureManageRepository lectureRepository;
  private final ModelMapper modelMapper;

  public LectureManageService(LectureManageRepository lectureRepository, ModelMapper modelMapper) {
    this.lectureRepository = lectureRepository;
    this.modelMapper = modelMapper;
  }

  @Transactional(readOnly = true)
  public List<LectureDto> findAllLecture() {
    List<EntityLecture> lectures = this.lectureRepository.findByUseFlagIsTrueOrderByRegisteredAtDesc();
    return lectures.stream().map(l -> this.modelMapper.map(l, LectureDto.class)).toList();
  }

  @Transactional
  public LectureDto createLecture(CreateLectureRequest lectureRequest) {
    EntityLecture entityLecture = this.modelMapper.map(lectureRequest, EntityLecture.class);
    // 백오피스 관리자가 로그인했다고 가정
    entityLecture.setRegisteredUserId(100011L);
    EntityLecture savedLectures = this.lectureRepository.save(entityLecture);

    return this.modelMapper.map(savedLectures, LectureDto.class);
  }

  @Transactional(readOnly = true)
  public LectureEnrolledEmployeesDto findEnrolledEmployeesByLecture(Long lectureId) {
    EntityLecture entityLecture =
            this.lectureRepository.findByIdAndUseFlagIsTrue(lectureId).orElseThrow(() -> new LectureNotFoundException("존재하지 않는 강의입니다."));

    List<EntityEnrolledLectureEmployee> enrolledLectureEmployees = entityLecture.getEnrolledLectureEmployee()
            .stream()
            .filter(EntityEnrolledLectureEmployee::isUseFlag)
            .toList();

    LectureEnrolledEmployeesDto lectureEnrolledEmployeesDto = new LectureEnrolledEmployeesDto();
    lectureEnrolledEmployeesDto.setLecture(this.modelMapper.map(entityLecture, LectureDto.class));

    List<EnrolledEmployeeDto> enrolledEmployeeDtoList = new ArrayList<>();
    for (EntityEnrolledLectureEmployee enrolledLectureEmployee : enrolledLectureEmployees) {
      EntityEmployee employee = enrolledLectureEmployee.getEmployee();
      EnrolledEmployeeDto enrolledEmployeeDto = new EnrolledEmployeeDto();
      enrolledEmployeeDto.setEmployeeId(employee.getId());
      enrolledEmployeeDto.setEmployeeNo(employee.getEmployeeNo());
      enrolledEmployeeDto.setRegisteredAt(enrolledLectureEmployee.getRegisteredAt());
      enrolledEmployeeDto.setUseFlag(enrolledLectureEmployee.isUseFlag());
      enrolledEmployeeDtoList.add(enrolledEmployeeDto);
    }

    lectureEnrolledEmployeesDto.setEnrolledEmployees(enrolledEmployeeDtoList);
    lectureEnrolledEmployeesDto.setEnrolledEmployees(enrolledEmployeeDtoList);

    return lectureEnrolledEmployeesDto;
  }
}
