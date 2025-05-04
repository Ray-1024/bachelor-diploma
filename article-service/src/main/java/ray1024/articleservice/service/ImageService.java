package ray1024.articleservice.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ray1024.articleservice.configuration.S3Configuration;
import ray1024.articleservice.exception.ImageSavingException;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {
    private S3Configuration s3Config;
    private MinioClient minioClient;

    public String saveImage(byte[] file) {
        String name = UUID.randomUUID().toString();
        String bucket = s3Config.getBucket();
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(name)
                    .stream(new ByteArrayInputStream(file), file.length, file.length)
                    .build()
            );
        } catch (Exception e) {
            throw new ImageSavingException(name);
        }
        return name;
    }
}
