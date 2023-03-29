package com.example.jclouds1gr.service;

import com.example.jclouds1gr.model.config.Provider;
import com.example.jclouds1gr.model.config.SyncMethod;
import lombok.AllArgsConstructor;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobBuilder;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.springframework.stereotype.Service;
import static com.example.jclouds1gr.model.config.SyncMethod.MERGE;

@Service
@AllArgsConstructor
public class CloudsService {

  private final S3Service s3Service;
  private final GcpService gcpService;


  public void syncBucketsBetweenClouds(Provider sourceProvider, String sourceBucketName,
                                       Provider destinationProvider,
                                       String destinationBucketName, SyncMethod syncMethod) {
    // Get the source context and BlobStore
    BlobStoreContext sourceContext = getContext(sourceProvider);

    // Get the destination context and BlobStore
    BlobStoreContext destinationContext = getContext(destinationProvider);

    // Get the source bucket and create destination bucket if it doesn't exist
    PageSet<? extends StorageMetadata> sourceBucketMetadata = sourceContext.getBlobStore().
      list(sourceBucketName, ListContainerOptions.NONE);
    boolean destinationBucketExists = destinationContext.getBlobStore().containerExists(destinationBucketName);
    if (!destinationBucketExists) {
      destinationContext.getBlobStore().createContainerInLocation(null, destinationBucketName);
    }

    // Copy each blob in the source bucket to the destination bucket if it doesn't exist in destination
    copyBlob(sourceBucketName, destinationBucketName, sourceContext.getBlobStore(),
      destinationContext.getBlobStore(), sourceBucketMetadata);

    if (syncMethod == MERGE) {
      // If merge is requested, also copy each blob from the destination bucket to the source bucket
      PageSet<? extends StorageMetadata> destinationBucketMetadata = destinationContext.getBlobStore().
        list(destinationBucketName, ListContainerOptions.NONE);

      copyBlob(destinationBucketName, sourceBucketName, destinationContext.getBlobStore(),
        sourceContext.getBlobStore(), destinationBucketMetadata);
    }

    // Close the JClouds contexts to release resources
    sourceContext.close();
    destinationContext.close();
  }

  private void copyBlob(String sourceBucketName, String destinationBucketName,
                        BlobStore sourceBlobStore, BlobStore destinationBlobStore,
                        PageSet<? extends StorageMetadata> bucketMetaData) {
    for (StorageMetadata sourceObjectMetadata : bucketMetaData) {
      String sourceObjectName = sourceObjectMetadata.getName();
      boolean objectExistsInDestination = destinationBlobStore.blobExists(destinationBucketName, sourceObjectName);
      if (!objectExistsInDestination) {
        Blob sourceBlob = sourceBlobStore.getBlob(sourceBucketName, sourceObjectName);
        PutOptions options = PutOptions.NONE;
        destinationBlobStore.putBlob(destinationBucketName, sourceBlob, options);
      }
    }
  }

  public void copyBlobBetweenClouds(Provider sourceProvider, String sourceBucketName, String blobName,
                                    Provider destinationProvider, String destinationBucketName) {


    BlobStoreContext sourceContext = getContext(sourceProvider);
    BlobStore sourceBlobStore = sourceContext.getBlobStore();

    BlobStoreContext destinationContext = getContext(destinationProvider);
    BlobStore destinationBlobStore = destinationContext.getBlobStore();

    Blob sourceBlob = sourceBlobStore.getBlob(sourceBucketName, blobName);


    Blob destinationBlob = destinationBlobStore.getBlob(destinationBucketName, blobName);

    if (destinationBlob == null) {
      destinationBlobStore.putBlob(destinationBucketName, sourceBlob, PutOptions.NONE);
    } else {
      BlobBuilder destinationBlobBuilder = destinationBlobStore.blobBuilder(blobName)
        .payload(sourceBlob.getPayload())
        .contentLength(sourceBlob.getMetadata().getContentMetadata().getContentLength());
      destinationBlobStore.putBlob(destinationBucketName, destinationBlobBuilder.build(), PutOptions.NONE);
    }

    sourceContext.close();
    destinationContext.close();
  }

  private BlobStoreContext getContext(Provider provider) {
    return switch (provider) {
      case AWS -> s3Service.createContextForAws(provider);
      case GCP -> gcpService.createContextForGcp(provider);
      default -> null;
    };
  }


}
