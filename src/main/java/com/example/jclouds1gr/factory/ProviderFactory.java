package com.example.jclouds1gr.factory;

import com.example.jclouds1gr.model.config.Provider;
import com.example.jclouds1gr.service.AzureService;
import com.example.jclouds1gr.service.GcpService;
import com.example.jclouds1gr.service.ProviderService;
import com.example.jclouds1gr.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProviderFactory {

  private final S3Service s3Service;
  private final GcpService gcpService;
  private final AzureService azureService;


  public ProviderService getProviderService(Provider provider) {
    return switch (provider) {
      case AWS -> s3Service;
      case GCP -> gcpService;
      case AZURE -> azureService;
      default -> null;
    };
  }


}
