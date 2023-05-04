package com.kstd.lecture.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "employee")
@Table(name = "employee")
public class EntityEmployee extends BaseEntity {
    private String employeeNo;

    @OneToMany(mappedBy = "employee")
    private List<EntityEnrolledLectureEmployee> enrolledLectureEmployee = new ArrayList<>();

    private boolean useFlag;
}
