package com.github.nancho313.videoloaderapi.infrastructure.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3Integrator {

  private final S3Client s3Client;

  private final String bucket;

  public S3Integrator(@Value("${aws.access-key}") String accessKey,
                      @Value("${aws.secret-key}") String secretKey,
                      @Value("${aws.session-token}") String sessionToken,
                      @Value("${aws.original-videos-bucket}") String bucket) {

    this.bucket = bucket;
    AwsCredentials credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);
    s3Client = S3Client
            .builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
  }

  public String storeVideo(byte[] video, String title) {

    RequestBody requestBody = RequestBody.fromBytes(video);
    PutObjectRequest objectRequest = PutObjectRequest.builder().bucket(bucket).key(title).build();
    s3Client.putObject(objectRequest, requestBody);
    var getUrlRequest = GetUrlRequest.builder().bucket(bucket).key(title).build();
    var response = s3Client.utilities().getUrl(getUrlRequest);
    return response.toExternalForm();
  }
}
