package lk.petalpink.petalpink.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Handles file uploads to Google Cloud Storage for the website side.
 * Configure credentials via application.properties or environment variables.
 */
@Service
public class WebsiteGcpStorageService {

    @Value("${gcp.project-id:petal-pink}")
    private String projectId;

    @Value("${gcp.bucket-name:petal-pink}")
    private String bucketName;

    @Value("${gcp.credentials-path:utils/petal-pink-cfa7e6fb0d5b.json}")
    private String credentialsPath;

    /**
     * Uploads a file to GCP and returns the public URL.
     *
     * @param file     multipart file to upload
     * @param fileName destination path inside the bucket (e.g. "banners/123-image.jpg")
     * @return public GCS URL
     */
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .build()
                    .getService();

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType("image/jpeg")
                    .build();

            storage.create(blobInfo, file.getBytes());

            // Make the object public — alternatively, use signed URLs
            storage.createAcl(com.google.cloud.storage.BlobId.of(bucketName, fileName),
                    com.google.cloud.storage.Acl.of(com.google.cloud.storage.Acl.User.ofAllUsers(),
                            com.google.cloud.storage.Acl.Role.READER));

            return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to GCP: " + e.getMessage(), e);
        }
    }
}
