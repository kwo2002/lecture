package com.kstd.lecture.exception;

public class AlreadyEnrolledLectureException extends RuntimeException {
  public AlreadyEnrolledLectureException(String message) {
    super(message);
  }
}
