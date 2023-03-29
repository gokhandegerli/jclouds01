package com.example.jclouds1gr.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum StorageState {
  MASTER("MASTER"),
    SLAVE("SLAVE");

  private String value;

  StorageState(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static StorageState fromValue(String text) {
    for (StorageState b : StorageState.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
