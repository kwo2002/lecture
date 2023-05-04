package com.kstd.lecture.exception;

public class EnrollLimitExceedException extends RuntimeException {
  public EnrollLimitExceedException(String message) {
    super(message);
  }
}
