package com.kstd.lecture.common;

import static com.kstd.lecture.domain.common.ApiUtils.error;
import static java.util.stream.Collectors.joining;

import com.kstd.lecture.domain.common.ApiUtils;
import com.kstd.lecture.domain.common.ApiUtils.ApiResult;
import com.kstd.lecture.exception.AlreadyEnrolledLectureException;
import com.kstd.lecture.exception.EmployeeNotFoundException;
import com.kstd.lecture.exception.EnrollLimitExceedException;
import com.kstd.lecture.exception.LectureNotFoundException;
import com.kstd.lecture.exception.LockEnrollException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult requestValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining("\n"));

        return error(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectureNotFoundException.class)
    public ApiResult lectureNotFoundException(LectureNotFoundException e) {
        return error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ApiResult employeeNotFoundException(EmployeeNotFoundException e) {
        return error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyEnrolledLectureException.class)
    public ApiResult alreadyEnrolledException(AlreadyEnrolledLectureException e) {
        return error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EnrollLimitExceedException.class)
    public ApiResult enrollLimitExceedException(EnrollLimitExceedException e) {
        return error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockEnrollException.class)
    public ApiResult lockEnrollException(LockEnrollException e) {
        return error(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
