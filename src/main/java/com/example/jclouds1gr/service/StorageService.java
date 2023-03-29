package com.example.jclouds1gr.service;

import com.example.jclouds1gr.factory.ProviderFactory;
import com.example.jclouds1gr.model.config.Provider;
import com.example.jclouds1gr.model.config.SyncMethod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class StorageService {

  private final ProviderFactory providerFactory;
  private final CloudsService cloudsService;


  public void createBucket(String bucketName, Provider provider) {
    ProviderService service = providerFactory.getProviderService(provider);
    service.createBucket(bucketName, provider);
  }

  public void copyBucket(String sourceBucketName, String destinationBucketName, Provider provider) {
    ProviderService service = providerFactory.getProviderService(provider);
    service.copyBucket(sourceBucketName, destinationBucketName, provider);
  }

  public List<String> listBucket(Provider provider) {
    ProviderService service = providerFactory.getProviderService(provider);
    return service.listBucket(provider);
  }

  public String deleteBucket(String bucketName, Provider provider) {

    ProviderService service = providerFactory.getProviderService(provider);
    return service.deleteBucket(bucketName, provider);
  }

  public void uploadBlob(String bucketName, String objectName, MultipartFile file,
                         String contentType, Provider provider) throws IOException, InterruptedException {

    ProviderService service = providerFactory.getProviderService(provider);
    service.uploadBlob(bucketName, objectName, file, contentType, provider);
  }

  public List<String> listBlobs(String bucketName, Provider provider) {

    ProviderService service = providerFactory.getProviderService(provider);
    return service.listBlobs(bucketName, provider);
  }

  public void downloadBlob(String bucketName, String objectName,
                           String filePath, Provider provider) throws IOException {

    ProviderService service = providerFactory.getProviderService(provider);
    service.downloadBlob(bucketName, objectName, filePath, provider);
  }

  public void copyBlob(String sourceBucketName, String sourceObjectName, String destinationBucketName,
                       String destinationObjectName, Provider provider) throws FileNotFoundException {
    ProviderService service = providerFactory.getProviderService(provider);
    service.copyBlob(sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName, provider);
  }

  public void renameBlob(String bucketName, String oldObjectName, String newObjectName, Provider provider) {
    ProviderService service = providerFactory.getProviderService(provider);
    service.renameBlob(bucketName, oldObjectName, newObjectName, provider);
  }

  public String deleteBlob(String bucketName, String objectName, Provider provider) {
    ProviderService service = providerFactory.getProviderService(provider);
    return service.deleteBlob(bucketName, objectName, provider);
  }

  public void syncBucketsBetweenClouds(Provider sourceProvider, String sourceBucketName,
                                       Provider destinationProvider, String destinationBucketName,
                                       SyncMethod syncMethod) {

    cloudsService.syncBucketsBetweenClouds(sourceProvider, sourceBucketName,
      destinationProvider, destinationBucketName, syncMethod);
  }

  public void copyBlobBetweenClouds(Provider sourceProvider, String sourceBucketName,
                                    String blobName, Provider destinationProvider,
                                    String destinationBucketName) {

    cloudsService.copyBlobBetweenClouds(sourceProvider, sourceBucketName, blobName,
      destinationProvider, destinationBucketName);
  }
}
