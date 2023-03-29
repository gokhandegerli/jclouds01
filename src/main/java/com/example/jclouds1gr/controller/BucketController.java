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
import java.util.List;

@RestController
@RequestMapping("/buckets")
public class BucketController {

  private final StorageService storageService;

  public BucketController(StorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping()
  public ResponseEntity<String> createBucket(@RequestParam String bucketName,
                                             @RequestParam Provider provider) {
    storageService.createBucket(bucketName, provider);
    return ResponseEntity.ok("Bucket created successfully");
  }

  @PostMapping("/copy")
  public ResponseEntity<String> copyBucket(@RequestParam String sourceBucketName,
                                           @RequestParam String destinationBucketName,
                                           @RequestParam Provider provider) {
    storageService.copyBucket(sourceBucketName, destinationBucketName, provider);
    return ResponseEntity.ok("Bucket copied successfully");
  }


  @GetMapping()
  public List<String> listBucket(@RequestParam Provider provider) {

    return storageService.listBucket(provider);
  }

  @DeleteMapping()
  public ResponseEntity<?> deleteBucket(@RequestParam String bucketName,
                                        @RequestParam Provider provider) {

    return ResponseEntity.ok(storageService.deleteBucket(bucketName, provider));
  }


}
