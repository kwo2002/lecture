package com.kstd.lecture.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLectureRequest {
    @NotBlank(message = "강의자명은 필수입니다.")
    private String teacher;

    @NotBlank(message = "강연장은 필수입니다.")
    private String room;

    @Min(value = 1, message = "신청인원은 1명 이상이여야 합니다.")
    private int maxEnrollment;

    @Future(message = "강의시간은 과거일 수 없습니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lectureAt;

    private String lectureDesc;

    @Override
    public String toString() {
        return "LectureRequest{" +
                "teacher='" + teacher + '\'' +
                ", room='" + room + '\'' +
                ", maxEnrollment=" + maxEnrollment +
                ", lectureAt=" + lectureAt +
                ", lectureDesc='" + lectureDesc + '\'' +
                '}';
    }
}
