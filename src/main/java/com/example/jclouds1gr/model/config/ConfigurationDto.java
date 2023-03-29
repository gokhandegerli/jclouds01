package com.example.jclouds1gr.model.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.UUID;

/**
 * ConfigurationDto
 */

@Data
public class ConfigurationDto   {
  @JsonProperty("id")
  private UUID id = null;

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
