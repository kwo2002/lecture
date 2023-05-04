package com.kstd.lecture.manage.repository;

import com.kstd.lecture.domain.entity.EntityLecture;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureManageRepository extends JpaRepository<EntityLecture, Long> {

  List<EntityLecture> findByUseFlagIsTrueOrderByRegisteredAtDesc();

  @EntityGraph(attributePaths = {"enrolledLectureEmployee", "enrolledLectureEmployee.employee"})
  Optional<EntityLecture> findByIdAndUseFlagIsTrueOrderByRegisteredAtDesc(Long id);

}
