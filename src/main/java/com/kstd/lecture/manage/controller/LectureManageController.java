package com.kstd.lecture.manage.controller;

import static com.kstd.lecture.domain.common.ApiUtils.success;

import com.kstd.lecture.manage.service.LectureManageService;
import com.kstd.lecture.domain.common.ApiUtils.ApiResult;
import com.kstd.lecture.domain.dto.LectureEnrolledEmployeesDto;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.domain.dto.CreateLectureRequest;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/manage/lecture")
public class LectureManageController {
  private final LectureManageService lectureService;

  public LectureManageController(LectureManageService lectureService) {
    this.lectureService = lectureService;
  }

  @Description("강연목록 (전체 강연 목록)")
  @GetMapping
  public ApiResult<List<LectureDto>> getAllLectures() {
    List<LectureDto> allLecture = this.lectureService.findAllLecture();
    return success(allLecture);
  }

  @Description("강연 등록 (강연자, 강연장, 신청인원 강연시간, 강연내용)")
  @PostMapping
  public ApiResult<LectureDto> postCreateLecture(@RequestBody @Valid CreateLectureRequest lectureRequest) {
    LectureDto lecture = this.lectureService.createLecture(lectureRequest);
    return success(lecture);
  }

  @Description("강연 신청자 목록")
  @GetMapping("/{lectureId:\\d+}")
  public ApiResult<LectureEnrolledEmployeesDto> getEnrolledEmployees(@PathVariable("lectureId") Long lectureId) {
    return success(this.lectureService.findEnrolledEmployeesByLecture(lectureId));
  }

}
