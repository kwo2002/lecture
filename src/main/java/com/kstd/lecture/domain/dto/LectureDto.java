package com.kstd.lecture.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class LectureDto {
  private Long id;
  private String teacher;
  private String room;
  private int maxEnrollment;
  private int currentEnrollment;
  @JsonFormat(pattern = "yyyy.MM.dd")
  private LocalDate lectureAt;
  private String lectureDesc;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LectureDto that = (LectureDto) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "LectureDto{" +
            "id=" + id +
            ", teacher='" + teacher + '\'' +
            ", room='" + room + '\'' +
            ", maxEnrollment=" + maxEnrollment +
            ", lectureAt=" + lectureAt +
            ", lectureDesc='" + lectureDesc + '\'' +
            '}';
  }
}
