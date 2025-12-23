package com.example.board.util;

import com.example.board.constants.BoardConstants;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 날짜 처리 유틸리티 클래스.
 */
public class DateUtil {

  private static final DateTimeFormatter DATETIME_FORMATTER =
      DateTimeFormatter.ofPattern(BoardConstants.DATETIME_FORMAT);

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern(BoardConstants.DATE_FORMAT);

  /**
   * LocalDateTime을 문자열로 변환 (yyyy-MM-dd HH:mm:ss).
   *
   * @param dateTime LocalDateTime
   * @return 포맷팅된 문자열
   */
  public static String formatDateTime(LocalDateTime dateTime) {
    if (dateTime == null) {
      return "";
    }
    return dateTime.format(DATETIME_FORMATTER);
  }

  /**
   * LocalDate를 문자열로 변환 (yyyy-MM-dd).
   *
   * @param date LocalDate
   * @return 포맷팅된 문자열
   */
  public static String formatDate(LocalDate date) {
    if (date == null) {
      return "";
    }
    return date.format(DATE_FORMATTER);
  }

  /**
   * 문자열을 LocalDate로 변환 (yyyy-MM-dd).
   *
   * @param dateString 날짜 문자열
   * @return LocalDate 또는 null (파싱 실패 시)
   */
  public static LocalDate parseDate(String dateString) {
    if (dateString == null || dateString.trim().isEmpty()) {
      return null;
    }
    try {
      return LocalDate.parse(dateString, DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  /**
   * 문자열을 LocalDateTime으로 변환 (yyyy-MM-dd HH:mm:ss).
   *
   * @param dateTimeString 날짜시간 문자열
   * @return LocalDateTime 또는 null (파싱 실패 시)
   */
  public static LocalDateTime parseDateTime(String dateTimeString) {
    if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
      return null;
    }
    try {
      return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  /**
   * 인스턴스화 방지를 위한 private 생성자.
   */
  private DateUtil() {
    throw new AssertionError("Cannot instantiate utility class");
  }
}
