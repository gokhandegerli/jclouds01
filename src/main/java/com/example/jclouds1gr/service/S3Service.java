package com.example.jclouds1gr.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.jclouds1gr.config.ConfigClient;
import com.example.jclouds1gr.model.config.ConfigurationDto;
import com.example.jclouds1gr.model.config.Provider;
import lombok.AllArgsConstructor;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.s3.blobstore.S3BlobStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class S3Service implements ProviderService {

  private final ConfigClient configClient;

  @Override
  public void createBucket(String bucketName, Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    BlobStore blobStore = context.getBlobStore();
    var s3BlobStore = (S3BlobStore) blobStore;
    s3BlobStore.createContainerInLocation(null, bucketName);
    context.close();
  }

  @Override
  public void copyBucket(String sourceBucketName, String destinationBucketName, Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    copyBucketWithJclouds(sourceBucketName, destinationBucketName, context);
  }

  @Override
  public String deleteBucket(String bucketName, Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    return deleteBucketWithJclouds(bucketName, context);
  }

  @Override
  public List<String> listBucket(Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    BlobStore blobStore = context.getBlobStore();
    S3BlobStore s3BlobStore = S3BlobStore.class.cast(blobStore);
    PageSet<? extends StorageMetadata> containers = s3BlobStore.list();
    List<String> bucketNames = new ArrayList<>();
    for (StorageMetadata container : containers) {
      bucketNames.add(container.getName());
    }
    context.close();
    return bucketNames;
  }

  @Override
  public List<String> listBlobs(String bucketName, Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    return listBlobsWithJclouds(bucketName, context);
  }

  @Override
  public void uploadBlob(String bucketName, String objectName, MultipartFile file,
                         String contentType, Provider provider) throws IOException {

    BlobStoreContext context = createContextForAws(provider);
    uploadBlobWithJclouds(bucketName, objectName, file, contentType, context);
  }

  @Override
  public void downloadBlob(String bucketName, String objectName,
                           String filePath, Provider provider) throws IOException {
    BlobStoreContext context = createContextForAws(provider);
    downloadBlobWithJclouds(bucketName, objectName, filePath, context);
  }


  @Override
  public void copyBlob(String sourceBucketName, String sourceObjectName,
                       String destinationBucketName, String destinationObjectName,
                       Provider provider) throws FileNotFoundException {
    BlobStoreContext context = createContextForAws(provider);
    copyBlobWithJclouds(sourceBucketName, sourceObjectName, destinationBucketName, destinationObjectName, context);
  }

  @Override
  public void renameBlob(String bucketName, String oldObjectName, String newObjectName, Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    renameBlobWithJclouds(bucketName, oldObjectName, newObjectName, context);
  }

  @Override
  public String deleteBlob(String bucketName, String objectName, Provider provider) {
    BlobStoreContext context = createContextForAws(provider);
    return deleteBlobWithJclouds(bucketName, objectName, context);
  }

  public BlobStoreContext createContextForAws(Provider provider) {
    ConfigurationDto configuration = configClient.createConfiguration(provider);
    try {
      return ContextBuilder.newBuilder("aws-s3")
        .credentials(configuration.getKey(), configuration.getSecret())
        .endpoint("https://s3.amazonaws.com")
        .buildView(BlobStoreContext.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create the context", e);
    }
  }

}
