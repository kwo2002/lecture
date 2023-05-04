package com.kstd.lecture.domain.entity;


import com.kstd.lecture.exception.EnrollLimitExceedException;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "lecture")
@Table(name = "lecture")
public class EntityLecture extends BaseEntity {

  @OneToMany(mappedBy = "lecture")
  private List<EntityEnrolledLectureEmployee> enrolledLectureEmployee = new ArrayList<>();

  private String teacher;
  private String room;
  private Integer maxEnrollment;
  private Integer currentEnrollment = 0;
  private LocalDate lectureAt;
  private String lectureDesc;
  private boolean useFlag = true;

  @Version
  private Long version;

  public void increaseCurrentEnrollment() {
    validateEnrollment();
    currentEnrollment += 1;
  }

  public void decreaseCurrentEnrollment() {
    currentEnrollment -= 1;
  }

  private void validateEnrollment() {
    if (currentEnrollment + 1 > maxEnrollment) {
      throw new EnrollLimitExceedException("신청 인원이 초과하였습니다.");
    }
  }

}
