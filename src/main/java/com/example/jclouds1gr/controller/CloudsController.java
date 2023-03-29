package com.example.jclouds1gr.controller;

import com.example.jclouds1gr.model.config.Provider;
import com.example.jclouds1gr.model.config.SyncMethod;
import com.example.jclouds1gr.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/clouds")
public class CloudsController {

  private final StorageService storageService;

  public CloudsController(StorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping("/sync-buckets")
  public ResponseEntity<String> syncBucketsBetweenClouds(@RequestParam Provider sourceProvider,
                                                         @RequestParam String sourceBucketName,
                                                         @RequestParam Provider destinationProvider,
                                                         @RequestParam String destinationBucketName,
                                                         @RequestParam SyncMethod syncMethod) throws IOException {
    switch (syncMethod) {
      case ONEWAY -> {
        storageService.syncBucketsBetweenClouds(sourceProvider, sourceBucketName,
          destinationProvider, destinationBucketName, syncMethod);
        return ResponseEntity.ok("Bucket copied successfully from one cloud to another");
      }
      case MERGE -> {
        storageService.syncBucketsBetweenClouds(sourceProvider, sourceBucketName,
          destinationProvider, destinationBucketName, syncMethod);
        return ResponseEntity.ok("Buckets merged successfully");
      }
      default -> {
        return ResponseEntity.ok("Please provide correct information!");
      }
    }
  }


  @PostMapping("/copy-blob")
  public ResponseEntity<String> copyBlobBetweenClouds(@RequestParam Provider sourceProvider,
                                                      @RequestParam String sourceBucketName,
                                                      @RequestParam String blobName,
                                                      @RequestParam Provider destinationProvider,
                                                      @RequestParam String destinationBucketName) {
    storageService.copyBlobBetweenClouds(sourceProvider, sourceBucketName, blobName,
      destinationProvider, destinationBucketName);
    return ResponseEntity.ok("Blob copied successfully from one cloud to another");
  }





}
