package com.kstd.lecture.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrolledLectureDto {
  private LectureDto lecture;
  private EmployeeDto employee;
}
