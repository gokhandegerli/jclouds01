package com.example.jclouds1gr.config;

import com.example.jclouds1gr.model.config.ConfigurationDto;
import com.example.jclouds1gr.model.config.ConfigurationRequest;
import com.example.jclouds1gr.model.config.Provider;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ConfigCacheService {

  private final Map<Provider, ConfigurationDto> cachedConfigOfAProvider = new HashMap<>();
  private LocalDateTime cacheExpiration;
  private final Map<Provider, List<String>> providerCredentials = new HashMap<>();

  // Set the cache expiration time to any seconds from the current time
  private final Duration cacheDuration = Duration.ofSeconds(120);

  public ConfigCacheService() {
    // Start a background thread to periodically clear the cache
    Executors.newSingleThreadScheduledExecutor()
      .scheduleAtFixedRate(this::clearCacheIfNeeded, cacheDuration.getSeconds(),
        cacheDuration.getSeconds(), TimeUnit.SECONDS);
  }


  public synchronized Map<Provider, ConfigurationDto> setConfigurations(List<ConfigurationRequest> configRequests) {
    Map<Provider, ConfigurationDto> configurations = new HashMap<>();
    LocalDateTime expiration = LocalDateTime.now().plus(cacheDuration);
    for (ConfigurationRequest request : configRequests) {
      Provider provider = request.getProvider();
      ConfigurationDto configuration = new ConfigurationDto();
      configuration.setKey(request.getKey());
      configuration.setSecret(request.getSecret());
      configurations.put(provider, configuration);
    }
    cacheConfigurations(configurations, expiration);
    return configurations;
  }

  public synchronized void cacheConfigurations(Map<Provider, ConfigurationDto> configurations, LocalDateTime expiration) {
    cachedConfigOfAProvider.clear();
    cachedConfigOfAProvider.putAll(configurations);
    cacheExpiration = expiration;
  }

  public synchronized Map<Provider, ConfigurationDto> getCachedConfigurations() {
    if (cacheExpiration != null && cacheExpiration.isBefore(LocalDateTime.now())) {
      // Clear the cache if it has expired
      clearCache();
    }
    return cachedConfigOfAProvider;
  }

  public synchronized void clearCache() {
    cachedConfigOfAProvider.clear();
    providerCredentials.clear();
    cacheExpiration = null;
  }

  public synchronized List<String> getProviderCredentials(Provider provider) {
    if (cacheExpiration != null && cacheExpiration.isBefore(LocalDateTime.now())) {
      // Clear the cache if it has expired
      clearCache();
    }
    if (!providerCredentials.containsKey(provider)) {
      ConfigurationDto configuration = cachedConfigOfAProvider.get(provider);
      if (configuration != null) {
        // Cache the provider credentials
        List<String> credentials = Arrays.asList(configuration.getKey(), configuration.getSecret());
        providerCredentials.put(provider, credentials);
        return credentials;
      } else {
        return Collections.emptyList();
      }
    } else {
      return providerCredentials.get(provider);
    }
  }

  private synchronized void clearCacheIfNeeded() {
    if (cacheExpiration != null && cacheExpiration.isBefore(LocalDateTime.now())) {
      clearCache();
    }

//  @Scheduled(fixedRate = 30)
//  public synchronized void clearCacheIfNeeded() {
//    if (cacheExpiration != null && cacheExpiration.isBefore(LocalDateTime.now())) {
//      clearCache();
//    }
  }

}


