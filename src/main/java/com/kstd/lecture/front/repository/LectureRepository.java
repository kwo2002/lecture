package com.kstd.lecture.front.repository;

import com.kstd.lecture.domain.entity.EntityLecture;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<EntityLecture, Long> {

  List<EntityLecture> findByUseFlagIsTrue();

  Optional<EntityLecture> findByIdAndUseFlagIsTrue(Long lectureId);

  List<EntityLecture> findByLectureAtBetweenAndUseFlagIsTrueOrderByRegisteredAtDesc(LocalDate start, LocalDate end);

}
