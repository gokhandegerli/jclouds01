package com.example.jclouds1gr.config;

import com.example.jclouds1gr.model.config.ConfigurationDto;
import com.example.jclouds1gr.model.config.Provider;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ConfigService implements ConfigClient {

  private final ConfigCacheService configCacheService;

  public ConfigService(ConfigCacheService configCacheService) {
    this.configCacheService = configCacheService;
  }

  @Override
  public ConfigurationDto createConfiguration(Provider provider) {
    List<String> credentials = configCacheService.getProviderCredentials(provider);
    ConfigurationDto config = new ConfigurationDto();
    config.setKey(credentials.get(0));
    config.setSecret(credentials.get(1));
    return config;
  }

}


