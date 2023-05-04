package com.kstd.lecture.front.repository;

import com.kstd.lecture.domain.entity.EntityEmployee;
import com.kstd.lecture.domain.entity.EntityEnrolledLectureEmployee;
import com.kstd.lecture.domain.entity.EntityLecture;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrolledLectureEmployeeRepository extends JpaRepository<EntityEnrolledLectureEmployee, Long> {

  @EntityGraph(attributePaths = {"lecture"})
  List<EntityEnrolledLectureEmployee> findByRegisteredAtGreaterThanEqualOrderByRegisteredAtDesc(LocalDateTime registeredAt);

  @EntityGraph(attributePaths = {"lecture"})
  List<EntityEnrolledLectureEmployee> findByEmployeeAndUseFlagIsTrue(EntityEmployee employee);

  Optional<EntityEnrolledLectureEmployee> findByLectureAndEmployeeAndUseFlagIsTrue(EntityLecture lecture, EntityEmployee employee);
}
