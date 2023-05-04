package com.kstd.lecture.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollLectureRequest {
  @Size(min = 5, max = 5, message = "다섯 자리 사번을 입력하세요.")
  private String employeeNo;
}
