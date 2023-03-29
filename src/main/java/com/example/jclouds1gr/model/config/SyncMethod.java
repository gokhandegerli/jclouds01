package com.example.jclouds1gr.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Synchronization Method
 */
public enum SyncMethod {
  ONEWAY("ONEWAY"),
    MERGE("MERGE"),
    NONE("NONE");

  private String value;

  SyncMethod(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SyncMethod fromValue(String text) {
    for (SyncMethod b : SyncMethod.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
