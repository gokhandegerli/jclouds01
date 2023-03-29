package com.example.jclouds1gr.controller;

import com.example.jclouds1gr.model.config.Provider;
import com.example.jclouds1gr.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/blobs")
public class BlobController {

  private final StorageService storageService;

  public BlobController(StorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadBlob(@RequestParam String bucketName,
                                           @RequestParam String objectName,
                                           @RequestParam MultipartFile file,
                                           @RequestParam String contentType,
                                           @RequestParam Provider provider) throws IOException, InterruptedException {

    storageService.uploadBlob(bucketName, objectName, file, contentType, provider);
    return ResponseEntity.ok("Blob uploaded successfully");
  }

  @GetMapping()
  public ResponseEntity<List<String>> listBlobs(@RequestParam String bucketName,
                                                @RequestParam Provider provider) {

    return ResponseEntity.ok(storageService.listBlobs(bucketName, provider));
  }


  @GetMapping("/download")
  public ResponseEntity<String> downloadBlob(@RequestParam String bucketName,
                                             @RequestParam String objectName,
                                             @RequestParam String filePath,
                                             @RequestParam Provider provider) throws IOException {

    storageService.downloadBlob(bucketName, objectName, filePath, provider);
    return ResponseEntity.ok("Blob downloaded successfully");

  }

  @PostMapping("/copy")
  public ResponseEntity<String> copyBlob(@RequestParam String sourceBucketName,
                                         @RequestParam String sourceObjectName,
                                         @RequestParam String destinationBucketName,
                                         @RequestParam String destinationObjectName,
                                         @RequestParam Provider provider) throws FileNotFoundException {
    storageService.copyBlob(sourceBucketName, sourceObjectName,
      destinationBucketName, destinationObjectName, provider);
    return ResponseEntity.ok("Blob copied successfully");

  }

  @PostMapping("/rename")
  public ResponseEntity<String> renameBlob(@RequestParam String bucketName,
                         @RequestParam String oldObjectName,
                         @RequestParam String newObjectName,
                         @RequestParam Provider provider) {

    storageService.renameBlob(bucketName, oldObjectName, newObjectName, provider);
    return ResponseEntity.ok("Blob renamed successfully");
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteBlob(@RequestParam String bucketName,
                                      @RequestParam String objectName,
                                      @RequestParam Provider provider) {
    return ResponseEntity.ok(storageService.deleteBlob(bucketName, objectName, provider));
  }
}
