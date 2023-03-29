package com.example.jclouds1gr.service;

import com.example.jclouds1gr.model.config.Provider;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobBuilder;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.options.CopyOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.io.Payload;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public interface ProviderService {

  void createBucket(String bucketName, Provider provider);

  default void createBucketWithJclouds(String bucketName, BlobStoreContext context) {

    context.getBlobStore().createContainerInLocation(null, bucketName);
    context.close();
  }

  void copyBucket(String sourceBucketName, String destinationBucketName, Provider provider);

  default void copyBucketWithJclouds(String sourceBucketName, String destinationBucketName,
                                     BlobStoreContext context) {

    PageSet<? extends StorageMetadata> sourceObjects = context.getBlobStore().list(sourceBucketName);

    context.getBlobStore().createContainerInLocation(null, destinationBucketName);

    for (StorageMetadata sourceObject : sourceObjects) {
      String sourceObjectName = sourceObject.getName();
      Blob sourceBlob = context.getBlobStore().getBlob(sourceBucketName, sourceObjectName);
      context.getBlobStore().putBlob(destinationBucketName, sourceBlob);
    }
    context.close();
  }


  String deleteBucket(String bucketName, Provider provider);

  default String deleteBucketWithJclouds(String bucketName, BlobStoreContext context) {

    if (context.getBlobStore().containerExists(bucketName)) {
      context.getBlobStore().deleteContainer(bucketName);
      context.close();
      return "Deleted";
    } else {
      context.close();
      return ("Bucket " + bucketName + " does not exist.");
    }
  }
  List<String> listBucket(Provider provider);

  default List<String> listBucketWithJclouds(BlobStoreContext context) {
    List<String> bucketList = new ArrayList<>();
    PageSet<? extends StorageMetadata> containers = context.getBlobStore().list();
    for (StorageMetadata container : containers) {
      bucketList.add(container.getName());
    }
    context.close();
    return bucketList;
  }


  List<String> listBlobs(String bucketName, Provider provider);

  default List<String> listBlobsWithJclouds(String bucketName, BlobStoreContext context) {

    PageSet<? extends StorageMetadata> pageSet = context.getBlobStore().list(bucketName);

    List<String> blobNames = new ArrayList<>();
    for (StorageMetadata metadata : pageSet) {
      if (metadata.getName() != null) {
        blobNames.add(metadata.getName());
      }
    }
    context.close();
    return blobNames;
  }


  void uploadBlob(String bucketName, String objectName, MultipartFile file,
                  String contentType, Provider provider) throws InterruptedException, IOException;

  default void uploadBlobWithJclouds(String bucketName, String objectName, MultipartFile file,
                                     String contentType, BlobStoreContext context) throws IOException {

    byte[] fileBytes = file.getBytes();
    BlobBuilder blobBuilder = context.getBlobStore().blobBuilder(objectName)
      .payload(fileBytes)
      .contentLength(fileBytes.length)
      .contentType(contentType);

    Blob blob = blobBuilder.build();
    context.getBlobStore().putBlob(bucketName, blob, PutOptions.Builder.multipart());
    context.close();
  }


  void downloadBlob(String bucketName, String objectName, String filePath,
                    Provider provider) throws IOException;

  default void downloadBlobWithJclouds(String bucketName, String objectName, String filePath,
                                       BlobStoreContext context) throws IOException {
    Blob blob = context.getBlobStore().getBlob(bucketName, objectName);
    if (blob == null) {
      throw new FileNotFoundException("Blob not found in the bucket");
    }
    Payload payload = blob.getPayload();
    try (InputStream inputStream = payload.openStream()) {
      Files.copy(inputStream, Paths.get(filePath));
    }
    context.close();
  }

  void copyBlob(String sourceBucketName, String sourceObjectName,
                String destinationBucketName, String destinationObjectName,
                Provider provider) throws FileNotFoundException;

  default void copyBlobWithJclouds(String sourceBucketName, String sourceObjectName,
                                   String destinationBucketName, String destinationObjectName,
                                   BlobStoreContext context) throws FileNotFoundException {
    Blob blob = context.getBlobStore().getBlob(sourceBucketName, sourceObjectName);
    if (blob == null) {
      throw new FileNotFoundException("Blob not found in source bucket");
    }
    context.getBlobStore().copyBlob(sourceBucketName, sourceObjectName,
      destinationBucketName, destinationObjectName, CopyOptions.NONE);
    context.close();
  }


  void renameBlob(String bucketName, String oldObjectName, String newObjectName, Provider provider);

  default void renameBlobWithJclouds(String bucketName, String oldObjectName,
                                     String newObjectName, BlobStoreContext context) {

    Blob oldBlob = context.getBlobStore().getBlob(bucketName, oldObjectName);
    BlobBuilder newBlobBuilder = context.getBlobStore().blobBuilder(newObjectName)
      .payload(oldBlob.getPayload())
      .contentLength(oldBlob.getMetadata().getContentMetadata().getContentLength())
      .contentType(oldBlob.getMetadata().getContentMetadata().getContentType());
    Blob newBlob = newBlobBuilder.build();
    context.getBlobStore().putBlob(bucketName, newBlob);
    context.getBlobStore().removeBlob(bucketName, oldObjectName);
    context.close();
  }


  String deleteBlob(String bucketName, String objectName, Provider provider);


  default String deleteBlobWithJclouds(String bucketName, String objectName, BlobStoreContext context) {

    if (context.getBlobStore().blobExists(bucketName, objectName)) {
      context.getBlobStore().removeBlob(bucketName, objectName);
      context.close();
      return "Blob " + objectName + " deleted from bucket " + bucketName;
    } else {
      context.close();
      return ("Blob " + objectName + " does not exist in bucket " +
        bucketName + " or bucket " + bucketName + " does not exist");
    }
  }
}








