package com.zenitnet.websiteconfigurator.domain.picture;

import com.zenitnet.websiteconfigurator.common.exception.TechnicalException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@CommonsLog
@Service
public class StorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String publicUrl;

    public StorageService(
        S3Client s3Client,
        @Qualifier("storageBucketName") String bucketName,
        @Qualifier("storagePublicUrl") String publicUrl
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.publicUrl = publicUrl;
    }

    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename);
        String key = UUID.randomUUID() + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (S3Exception e) {
            throw new TechnicalException("Failed to upload file to storage", e);
        } catch (IOException e) {
            throw new TechnicalException("Failed to read file content", e);
        }

        return buildPublicUrl(key);
    }

    public void deleteFile(String url) {
        String key = extractKeyFromUrl(url);

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new TechnicalException("Failed to delete file from storage", e);
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private String buildPublicUrl(String key) {
        String normalizedUrl = publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
        return normalizedUrl + "/" + key;
    }

    private String extractKeyFromUrl(String url) {
        String prefix = publicUrl.endsWith("/") ? publicUrl : publicUrl + "/";
        return url.replace(prefix, "");
    }
}
