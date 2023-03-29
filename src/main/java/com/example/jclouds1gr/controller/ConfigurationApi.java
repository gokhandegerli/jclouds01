package com.example.jclouds1gr.controller;

import com.example.jclouds1gr.config.ConfigCacheService;
import com.example.jclouds1gr.model.config.ConfigurationDto;
import com.example.jclouds1gr.model.config.ConfigurationRequest;
import com.example.jclouds1gr.model.config.Provider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/configs")
public class ConfigurationApi {

  private final ConfigCacheService configCacheService;

  @PutMapping()
  public Map<Provider, ConfigurationDto> setConfigs(@RequestBody List<ConfigurationRequest> configRequests) {
    return configCacheService.setConfigurations(configRequests);
  }

  @GetMapping()
  public ResponseEntity<Map<Provider, ConfigurationDto>> getCachedConfig() {
    Map<Provider, ConfigurationDto> cachedConfig = configCacheService.getCachedConfigurations();
    if (cachedConfig != null) {
      return new ResponseEntity<>(cachedConfig, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
  }

}
