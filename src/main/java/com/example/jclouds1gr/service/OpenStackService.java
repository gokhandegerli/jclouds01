package com.example.jclouds1gr.service;

import com.example.jclouds1gr.config.ConfigClient;
import com.example.jclouds1gr.model.config.ConfigurationDto;
import com.example.jclouds1gr.model.config.Provider;
import lombok.AllArgsConstructor;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class OpenStackService implements ProviderService {

  private final ConfigClient configClient;

  @Override
  public void createBucket(String bucketName, Provider provider) {

    BlobStoreContext context = createContextForOpenStack(provider);
    createBucketWithJclouds(bucketName, context);
  }

  @Override
  public void copyBucket(String sourceBucketName, String destinationBucketName, Provider provider) {
    BlobStoreContext context = createContextForOpenStack(provider);
    copyBucketWithJclouds(sourceBucketName, destinationBucketName, context);
  }

  @Override
  public String deleteBucket(String bucketName, Provider provider) {

    BlobStoreContext context = createContextForOpenStack(provider);
    return deleteBucketWithJclouds(bucketName, context);
  }

  @Override
  public List<String> listBucket(Provider provider) {

    BlobStoreContext context = createContextForOpenStack(provider);
    return listBucketWithJclouds(context);

  }

  @Override
  public List<String> listBlobs(String bucketName, Provider provider) {
    BlobStoreContext context = createContextForOpenStack(provider);
    return listBlobsWithJclouds(bucketName, context);
  }

  @Override
  public void uploadBlob(String bucketName, String objectName, MultipartFile file,
                         String contentType, Provider provider) throws IOException {
    BlobStoreContext context = createContextForOpenStack(provider);
    uploadBlobWithJclouds(bucketName, objectName, file, contentType, context);
  }

  @Override
  public void downloadBlob(String bucketName, String objectName, String filePath, Provider provider) throws IOException {
    BlobStoreContext context = createContextForOpenStack(provider);
    downloadBlobWithJclouds(bucketName, objectName, filePath, context);
  }

  @Override
  public void copyBlob(String sourceBucketName, String sourceObjectName, String destinationBucketName, String destinationObjectName, Provider provider) throws FileNotFoundException {
    BlobStoreContext context = createContextForOpenStack(provider);
    copyBlobWithJclouds(sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName, context);
  }

  @Override
  public void renameBlob(String bucketName, String oldObjectName, String newObjectName, Provider provider) {
    BlobStoreContext context = createContextForOpenStack(provider);
    renameBlobWithJclouds(bucketName, oldObjectName, newObjectName, context);
  }

  @Override
  public String deleteBlob(String bucketName, String objectName, Provider provider) {
    BlobStoreContext context = createContextForOpenStack(provider);
    return deleteBlobWithJclouds(bucketName, objectName, context);
  }

  public BlobStoreContext createContextForOpenStack(Provider provider) {
    ConfigurationDto configuration = configClient.createConfiguration(provider);
    try {
      return ContextBuilder.newBuilder("openstack-swift")
        .credentials(configuration.getKey(), configuration.getSecret())
        .buildView(BlobStoreContext.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create the context", e);
    }
  }
}