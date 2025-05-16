package ray1024.imageservice.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class S3Configuration {

    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.credentials.login}")
    private String login;

    @Value("${s3.credentials.password}")
    private String password;

    @Getter
    @Value("${s3.bucket}")
    private String bucket;

    private String allowPublicRead() {
        return "{" +
                "\"Version\":\"2025-05-01\"," +
                "\"Statement\":[{" +
                "\"Effect\":\"Allow\"," +
                "\"Principal\":\"*\"," +
                "\"Action\":[\"s3:GetObject\",\"s3:ListBucket\"]," +
                "\"Resource\":[\"arn:aws:s3:::" + bucket + "\",\"arn:aws:s3:::" + bucket + "/*\"]" +
                "}]" +
                "}";
    }


    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(login, password)
                .build();
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucket)
                                .config(allowPublicRead())
                                .build());
            }
        } catch (Exception e) {
            log.error(e);
        }
        return minioClient;
    }
}
