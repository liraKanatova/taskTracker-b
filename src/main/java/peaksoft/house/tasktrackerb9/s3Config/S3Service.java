package peaksoft.house.tasktrackerb9.s3Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class S3Service {
    
    @Value("${aws_bucket_name}")
    private String bucketName;
    @Value("${aws_bucket_path}")
    private String bucketPath;
    private final S3Client s3Client;

    public Map<String, String> upload(MultipartFile file) throws IOException {
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        PutObjectRequest p = PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType("JPG")
                .contentType("jpeg")
                .contentType("jfif")
                .contentType("png")
                .contentType("mp4")
                .contentType("ogg")
                .contentType("mpeg")
                .contentLength(file.getSize())
                .key(key)
                .build();
        s3Client.putObject(p, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return Map.of("Link", bucketPath + key);
    }

    public Map<String, String> delete(String fileLink) {
        try {
            String key = fileLink.substring(bucketPath.length());
            s3Client.deleteObject(dor -> dor.bucket(bucketName).key(key).build());
        } catch (S3Exception e) {
            throw new IllegalStateException(e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return Map.of("message", fileLink + " has been deleted");
    }

}

