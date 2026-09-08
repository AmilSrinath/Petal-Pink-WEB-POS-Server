package lk.petalpink.petalpink.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class WebsiteGcpStorageService {

    @Value("${gcp.project-id:petal-pink-official-website}")
    private String projectId;

    @Value("${gcp.bucket-name:petal-pink}")
    private String bucketName;

    @Value("${gcp.credentials-path:utils/petal-pink-cfa7e6fb0d5b.json}")
    private String credentialsPath;

    private Storage storage;

    private Storage getStorage() throws IOException {
        if (storage == null) {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(credentialsPath))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");

            storage = StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();
        }
        return storage;
    }

    // ✅ පරණ upload method එක තවමත් තියෙනවා (අනිත් තැන් වල use වෙනවා නම්)
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            Storage st = getStorage();
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType("image/jpeg")
                    .build();
            st.create(blobInfo, file.getBytes());
            st.createAcl(
                    BlobId.of(bucketName, fileName),
                    Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
            return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to GCP: " + e.getMessage(), e);
        }
    }

    // ✅ NEW: Signed PUT URL generate කරනවා — frontend එක direct GCS වලට upload කරන්න
    public SignedUrlResult generateSignedUploadUrl(String originalFileName, String contentType) {
        try {
            Storage st = getStorage();

            String safeExt = guessExtension(originalFileName, contentType);
            String objectName = "product/" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID() + safeExt;

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                    .setContentType(contentType != null ? contentType : "image/jpeg")
                    .build();

            URL signedUrl = st.signUrl(
                    blobInfo,
                    15, TimeUnit.MINUTES,
                    Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                    Storage.SignUrlOption.withV4Signature(),
                    Storage.SignUrlOption.withExtHeaders(java.util.Map.of(
                            "Content-Type", contentType != null ? contentType : "image/jpeg",
                            "x-goog-acl", "public-read"   // ✅ upload වුණු ගමන් public කරනවා
                    ))
            );

            // ❌ createAcl call එක මෙතනින් ඉවත් කරනවා — object තවම නෑ
            String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + objectName;

            return new SignedUrlResult(signedUrl.toString(), publicUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate signed URL: " + e.getMessage(), e);
        }
    }

    private String guessExtension(String originalFileName, String contentType) {
        if (originalFileName != null && originalFileName.contains(".")) {
            return originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        if (contentType != null) {
            if (contentType.contains("png")) return ".png";
            if (contentType.contains("webp")) return ".webp";
        }
        return ".jpg";
    }

    public record SignedUrlResult(String uploadUrl, String publicUrl) {}
}