package com.github.jamiebuckley.kalahva.util;

public class ObjectUtils {
  public static void notNullOrThrow(Object o, String message) {
    if (o == null) {
      throw new IllegalArgumentException(message);
    }
  }
}
