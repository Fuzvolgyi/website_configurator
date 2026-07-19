package com.zenitnet.websiteconfigurator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class StorageConfig {

    @Value("${storage.endpoint}")
    private String endpoint;

    @Value("${storage.access-key}")
    private String accessKey;

    @Value("${storage.secret-key}")
    private String secretKey;

    @Value("${storage.bucket-name}")
    private String bucketName;

    @Value("${storage.public-url}")
    private String publicUrl;

    @Value("${storage.region}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .forcePathStyle(true)
            .build();
    }

    @Bean("storageBucketName")
    public String storageBucketName() {
        return bucketName;
    }

    @Bean("storageEndpoint")
    public String storageEndpoint() {
        return endpoint;
    }

    @Bean("storagePublicUrl")
    public String storagePublicUrl() {
        return publicUrl;
    }
}
