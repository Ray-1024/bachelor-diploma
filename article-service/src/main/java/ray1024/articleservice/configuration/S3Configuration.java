package ray1024.articleservice.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
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


    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(login, password)
                .build();
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            log.error(e);
        }
        return minioClient;
    }
}
