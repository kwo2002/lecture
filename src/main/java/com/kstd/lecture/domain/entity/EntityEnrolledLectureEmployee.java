package com.kstd.lecture.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "enrolled_lecture_employee")
@Table(name = "enrolled_lecture_employee")
public class EntityEnrolledLectureEmployee extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "employee_id")
  private EntityEmployee employee;

  @ManyToOne
  @JoinColumn(name = "lecture_id")
  private EntityLecture lecture;

  private boolean useFlag = true;
  public void setEmployeeAndLecture(EntityEmployee employee, EntityLecture lecture) {
    if (this.employee != null) {
      this.employee.getEnrolledLectureEmployee().remove(this);
    }
    this.employee = employee;
    if (!employee.getEnrolledLectureEmployee().contains(this)) {
      employee.getEnrolledLectureEmployee().add(this);
    }

    if (this.lecture != null) {
      this.lecture.getEnrolledLectureEmployee().remove(this);
    }
    this.lecture = lecture;
    if (!lecture.getEnrolledLectureEmployee().contains(this)) {
      lecture.getEnrolledLectureEmployee().add(this);
    }
  }

}
