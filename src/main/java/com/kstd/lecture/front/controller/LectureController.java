package com.kstd.lecture.front.controller;

import static com.kstd.lecture.domain.common.ApiUtils.success;

import com.kstd.lecture.domain.common.ApiUtils.ApiResult;
import com.kstd.lecture.domain.dto.EnrolledLectureDto;
import com.kstd.lecture.domain.dto.EmployeeEnrolledLecturesDto;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.exception.LockEnrollException;
import com.kstd.lecture.front.service.LectureService;
import java.util.List;

import org.springframework.context.annotation.Description;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/lecture")
public class LectureController {
  private final LectureService lectureService;

  public LectureController(LectureService lectureService) {
    this.lectureService = lectureService;
  }

  @Description("신청가능한 강연 목록")
  @GetMapping
  public ApiResult<List<LectureDto>> getAvailableLectures() {
    List<LectureDto> availableLectures = this.lectureService.findAvailableLectures();
    return success(availableLectures);
  }

  @Description("강연 신청")
  @PostMapping("/{lectureId:\\d+}/employee/{employeeNo}")
  public ApiResult<EnrolledLectureDto> postEnrollLecture(
      @PathVariable("lectureId") Long lectureId,
      @PathVariable("employeeNo") String employeeNo) {
    try {
      EnrolledLectureDto enrolledLectureDto = this.lectureService.saveEnrolledLectureEmployee(
          lectureId, employeeNo);

      return success(enrolledLectureDto);
    } catch (OptimisticLockingFailureException e) {
      throw new LockEnrollException("요청하신 신청을 다른 사용자가 선점했습니다. 다시 시도하세요.");
    }
  }

  @Description("사번으로 신청한 강연 목록 조회")
  @GetMapping("/employee/{employeeNo}")
  public ApiResult<EmployeeEnrolledLecturesDto> getEnrolledLectures(@PathVariable("employeeNo") String employeeNo) {
    EmployeeEnrolledLecturesDto enrolledLecturesDto = this.lectureService.findEnrolledLecturesByEmployeeNo(employeeNo);
    return success(enrolledLecturesDto);
  }

  @Description("신청한 강연 취소")
  @DeleteMapping("/{lectureId:\\d+}/employee/{employeeNo}")
  public ApiResult<EnrolledLectureDto> deleteEnrolledLecture(
      @PathVariable("lectureId") Long lectureId,
      @PathVariable("employeeNo") String employeeNo) {
    EnrolledLectureDto enrolledLectureDto = this.lectureService.deleteEnrolledLecture(lectureId, employeeNo);
    return success(enrolledLectureDto);
  }

  @Description("인기 강연 조회 (최근 3일간 가장 많이 신청된 강연)")
  @GetMapping("/favorite")
  public ApiResult<List<LectureDto>> getFavoriteLecturesForLast3Days() {
    List<LectureDto> favoriteLectures = this.lectureService.getFavoriteLectures();
    return success(favoriteLectures);
  }
}
