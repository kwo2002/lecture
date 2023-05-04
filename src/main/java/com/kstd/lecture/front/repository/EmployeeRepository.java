package com.kstd.lecture.front.repository;

import com.kstd.lecture.domain.entity.EntityEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EntityEmployee, Long> {
  Optional<EntityEmployee> findByEmployeeNoAndUseFlagIsTrue(String employeeNo);
}
