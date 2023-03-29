package com.example.jclouds1gr.config;

import com.example.jclouds1gr.model.config.ConfigurationDto;
import com.example.jclouds1gr.model.config.Provider;

public interface ConfigClient {

  ConfigurationDto createConfiguration(Provider provider);

}
