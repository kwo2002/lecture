package com.kstd.lecture.front.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kstd.lecture.config.BaseControllerTest;
import com.kstd.lecture.domain.dto.EnrolledLectureDto;
import com.kstd.lecture.domain.dto.EmployeeEnrolledLecturesDto;
import com.kstd.lecture.domain.dto.LectureDto;
import com.kstd.lecture.front.service.LectureService;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(value = LectureController.class)
public class LectureControllerTest extends BaseControllerTest {
  @MockBean
  private LectureService lectureService;
  @Autowired
  private ObjectMapper objectMapper;

  @ParameterizedTest
  @MethodSource("testsource.lecture.LectureSource#lectureDto")
  void get_lecture(LectureDto lectureDto) throws Exception {
    given(this.lectureService.findAvailableLectures())
            .willReturn(List.of(lectureDto));

    ResultActions perform = this.mockMvc.perform(get("/api/lecture")
            .contentType(MediaType.APPLICATION_JSON));

    perform
            .andExpect(status().isOk())
            .andDo(
                    document("{method-name}",
                            responseFields(
                                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                    fieldWithPath("response").type(JsonFieldType.ARRAY).description("응답 data").optional(),
                                    fieldWithPath("response[].id").type(JsonFieldType.NUMBER).description("강의 ID"),
                                    fieldWithPath("response[].teacher").type(JsonFieldType.STRING).description("강사"),
                                    fieldWithPath("response[].room").type(JsonFieldType.STRING).description("강의실"),
                                    fieldWithPath("response[].maxEnrollment").type(JsonFieldType.NUMBER).description("강의 입장 가능 인원"),
                                    fieldWithPath("response[].currentEnrollment").type(JsonFieldType.NUMBER).description("현재 수강 신청 인원"),
                                    fieldWithPath("response[].lectureAt").type(JsonFieldType.STRING).description("강의 시간"),
                                    fieldWithPath("response[].lectureDesc").type(JsonFieldType.STRING).description("강의 내용"),
                                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("예외 응답").optional(),
                                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("예외 메세지"),
                                    fieldWithPath("error.status").type(JsonFieldType.NUMBER).description("예외 상태")
                            )
                    ))
            .andDo(print());
  }

  @ParameterizedTest
  @MethodSource("testsource.lecture.LectureSource#enrolledLectureDto")
  void post_enrollLecture(EnrolledLectureDto enrolledLectureDto) throws Exception {
    given(this.lectureService.saveEnrolledLectureEmployee(anyLong(), any()))
            .willReturn(enrolledLectureDto);

    ResultActions perform = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/lecture/{lectureId}/employee/{employeeNo}", 1L, "A1010")
            .contentType(MediaType.APPLICATION_JSON)
    );

    perform
            .andExpect(status().isOk())
            .andDo(
                    document("{method-name}",
                            pathParameters(
                                    parameterWithName("lectureId").description("강의 ID"),
                                    parameterWithName("employeeNo").description("사번")
                            ),
                            responseFields(
                                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                    fieldWithPath("response").type(JsonFieldType.OBJECT).description("응답 data").optional(),
                                    fieldWithPath("response.lecture").type(JsonFieldType.OBJECT).description("강의 상세"),
                                    fieldWithPath("response.lecture.id").type(JsonFieldType.NUMBER).description("강의 ID"),
                                    fieldWithPath("response.lecture.teacher").type(JsonFieldType.STRING).description("강사"),
                                    fieldWithPath("response.lecture.room").type(JsonFieldType.STRING).description("강의실"),
                                    fieldWithPath("response.lecture.maxEnrollment").type(JsonFieldType.NUMBER).description("강의 입장 가능 인원"),
                                    fieldWithPath("response.lecture.currentEnrollment").type(JsonFieldType.NUMBER).description("현재 수강 신청 인원"),
                                    fieldWithPath("response.lecture.lectureAt").type(JsonFieldType.STRING).description("강의 시간 yyyy-MM-dd"),
                                    fieldWithPath("response.lecture.lectureDesc").type(JsonFieldType.STRING).description("강의 내용").optional(),
                                    fieldWithPath("response.employee").type(JsonFieldType.OBJECT).description("신청자 목록"),
                                    fieldWithPath("response.employee.employeeId").type(JsonFieldType.NUMBER).description("직원 고유번호"),
                                    fieldWithPath("response.employee.employeeNo").type(JsonFieldType.STRING).description("사번"),
                                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("예외 응답").optional(),
                                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("예외 메세지"),
                                    fieldWithPath("error.status").type(JsonFieldType.NUMBER).description("예외 상태")
                            )
                    ))
            .andDo(print());
  }

  @ParameterizedTest
  @MethodSource("testsource.lecture.LectureSource#enrolledLecturesDto")
  void get_enrolledLecturesByEmployeeNo(EmployeeEnrolledLecturesDto enrolledLecturesDto) throws Exception {
    given(this.lectureService.findEnrolledLecturesByEmployeeNo(anyString()))
            .willReturn(enrolledLecturesDto);

    ResultActions perform = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/lecture/employee/{employeeNo}", "A1010")
            .contentType(MediaType.APPLICATION_JSON)
    );

    perform
            .andExpect(status().isOk())
            .andDo(
                    document("{method-name}",
                            pathParameters(
                                    parameterWithName("employeeNo").description("사번")
                            ),
                            responseFields(
                                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                    fieldWithPath("response").type(JsonFieldType.OBJECT).description("응답 data").optional(),
                                    fieldWithPath("response.lectures[]").type(JsonFieldType.ARRAY).description("강의 상세"),
                                    fieldWithPath("response.lectures[].id").type(JsonFieldType.NUMBER).description("강의 ID"),
                                    fieldWithPath("response.lectures[].teacher").type(JsonFieldType.STRING).description("강사"),
                                    fieldWithPath("response.lectures[].room").type(JsonFieldType.STRING).description("강의실"),
                                    fieldWithPath("response.lectures[].maxEnrollment").type(JsonFieldType.NUMBER).description("강의 입장 가능 인원"),
                                    fieldWithPath("response.lectures[].currentEnrollment").type(JsonFieldType.NUMBER).description("현재 수강 신청 인원"),
                                    fieldWithPath("response.lectures[].lectureAt").type(JsonFieldType.STRING).description("강의 시간 yyyy-MM-dd"),
                                    fieldWithPath("response.lectures[].lectureDesc").type(JsonFieldType.STRING).description("강의 내용").optional(),
                                    fieldWithPath("response.employee").type(JsonFieldType.OBJECT).description("신청자 목록"),
                                    fieldWithPath("response.employee.employeeId").type(JsonFieldType.NUMBER).description("직원 고유번호"),
                                    fieldWithPath("response.employee.employeeNo").type(JsonFieldType.STRING).description("사번"),
                                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("예외 응답").optional(),
                                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("예외 메세지"),
                                    fieldWithPath("error.status").type(JsonFieldType.NUMBER).description("예외 상태")
                            )
                    ))
            .andDo(print());
  }

  @ParameterizedTest
  @MethodSource("testsource.lecture.LectureSource#enrolledLectureDto")
  void delete_enrollLecture(EnrolledLectureDto enrolledLectureDto) throws Exception {
    given(this.lectureService.deleteEnrolledLecture(anyLong(), anyString()))
        .willReturn(enrolledLectureDto);

    ResultActions perform = this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/lecture/{lectureId}/employee/{employeeNo}", 1L, "A1010")
        .contentType(MediaType.APPLICATION_JSON)
    );

    perform
        .andExpect(status().isOk())
        .andDo(
            document("{method-name}",
                pathParameters(
                    parameterWithName("lectureId").description("강의 ID"),
                    parameterWithName("employeeNo").description("사번")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                    fieldWithPath("response").type(JsonFieldType.OBJECT).description("응답 data").optional(),
                    fieldWithPath("response.lecture").type(JsonFieldType.OBJECT).description("강의 상세"),
                    fieldWithPath("response.lecture.id").type(JsonFieldType.NUMBER).description("강의 ID"),
                    fieldWithPath("response.lecture.teacher").type(JsonFieldType.STRING).description("강사"),
                    fieldWithPath("response.lecture.room").type(JsonFieldType.STRING).description("강의실"),
                    fieldWithPath("response.lecture.maxEnrollment").type(JsonFieldType.NUMBER).description("강의 입장 가능 인원"),
                    fieldWithPath("response.lecture.currentEnrollment").type(JsonFieldType.NUMBER).description("현재 수강 신청 인원"),
                    fieldWithPath("response.lecture.lectureAt").type(JsonFieldType.STRING).description("강의 시간 yyyy-MM-dd"),
                    fieldWithPath("response.lecture.lectureDesc").type(JsonFieldType.STRING).description("강의 내용").optional(),
                    fieldWithPath("response.employee").type(JsonFieldType.OBJECT).description("신청자 목록"),
                    fieldWithPath("response.employee.employeeId").type(JsonFieldType.NUMBER).description("직원 고유번호"),
                    fieldWithPath("response.employee.employeeNo").type(JsonFieldType.STRING).description("사번"),
                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("예외 응답").optional(),
                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("예외 메세지"),
                    fieldWithPath("error.status").type(JsonFieldType.NUMBER).description("예외 상태")
                )
            ))
        .andDo(print());
  }

  @ParameterizedTest
  @MethodSource("testsource.lecture.LectureSource#lectureDto")
  void get_favoriteLecture(LectureDto lectureDto) throws Exception {
    given(this.lectureService.getFavoriteLectures())
        .willReturn(List.of(lectureDto));

    ResultActions perform = this.mockMvc.perform(get("/api/lecture/favorite")
        .contentType(MediaType.APPLICATION_JSON));

    perform
        .andExpect(status().isOk())
        .andDo(
            document("{method-name}",
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                    fieldWithPath("response").type(JsonFieldType.ARRAY).description("응답 data").optional(),
                    fieldWithPath("response[].id").type(JsonFieldType.NUMBER).description("강의 ID"),
                    fieldWithPath("response[].teacher").type(JsonFieldType.STRING).description("강사"),
                    fieldWithPath("response[].room").type(JsonFieldType.STRING).description("강의실"),
                    fieldWithPath("response[].maxEnrollment").type(JsonFieldType.NUMBER).description("강의 입장 가능 인원"),
                    fieldWithPath("response[].currentEnrollment").type(JsonFieldType.NUMBER).description("최근 3일 강연 신청 인원"),
                    fieldWithPath("response[].lectureAt").type(JsonFieldType.STRING).description("강의 시간"),
                    fieldWithPath("response[].lectureDesc").type(JsonFieldType.STRING).description("강의 내용"),
                    fieldWithPath("error").type(JsonFieldType.OBJECT).description("예외 응답").optional(),
                    fieldWithPath("error.message").type(JsonFieldType.STRING).description("예외 메세지"),
                    fieldWithPath("error.status").type(JsonFieldType.NUMBER).description("예외 상태")
                )
            ))
        .andDo(print());
  }
}
