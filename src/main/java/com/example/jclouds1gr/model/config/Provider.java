package com.example.jclouds1gr.model.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Cloud Providers
 */
public enum Provider {
  AWS("AWS"),
    AZURE("AZURE"),
    GCP("GCP");

  private String value;

  Provider(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Provider fromValue(String text) {
    for (Provider b : Provider.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
