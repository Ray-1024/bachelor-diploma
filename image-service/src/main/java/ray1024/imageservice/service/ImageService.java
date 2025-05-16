package ray1024.imageservice.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ray1024.imageservice.exception.ImageSavingException;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class ImageService {
    @Getter
    @Value("${s3.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    public ImageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String saveImage(byte[] file) {
        String name = UUID.randomUUID().toString();
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
