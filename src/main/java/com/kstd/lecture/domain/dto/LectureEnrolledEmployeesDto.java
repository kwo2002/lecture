package com.kstd.lecture.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LectureEnrolledEmployeesDto {
  private LectureDto lecture;
  private List<EnrolledEmployeeDto> enrolledEmployees = new ArrayList<>();
}
