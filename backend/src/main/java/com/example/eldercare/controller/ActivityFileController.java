package com.example.eldercare.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files/activities")
public class ActivityFileController {

    private static final String UPLOAD_DIR = "uploads/activities";

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        return getFile("images", filename, "image/jpeg");
    }

    @GetMapping("/videos/{filename:.+}")
    public ResponseEntity<Resource> getVideo(@PathVariable String filename) {
        return getFile("videos", filename, "video/mp4");
    }

    private ResponseEntity<Resource> getFile(String subDir, String filename, String defaultContentType) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, subDir, filename);
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = defaultContentType;
            String fileExtension = getFileExtension(filename);
            if (subDir.equals("images")) {
                switch (fileExtension.toLowerCase()) {
                    case "png":
                        contentType = "image/png";
                        break;
                    case "gif":
                        contentType = "image/gif";
                        break;
                    case "webp":
                        contentType = "image/webp";
                        break;
                }
            } else if (subDir.equals("videos")) {
                switch (fileExtension.toLowerCase()) {
                    case "mov":
                        contentType = "video/quicktime";
                        break;
                    case "webm":
                        contentType = "video/webm";
                        break;
                }
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }
}
