package com.example.jclouds1gr.model.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ConfigurationRequest
 */

@Data
public class ConfigurationRequest   {
  @JsonProperty("key")
  private String key = null;

  @JsonProperty("secret")
  private String secret = null;

  @JsonProperty("provider")
  private Provider provider = null;

  @JsonProperty("region")
  private String region = null;

  @JsonProperty("bucketName")
  private String bucketName = null;

  @JsonProperty("storageState")
  private StorageState storageState = null;

  @JsonProperty("syncMethod")
  private SyncMethod syncMethod = null;




}
