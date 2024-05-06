package com.mohamed.file;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload.photos-output-path}")
    private String fileUploadPath;

    // This method is used to save the file to the target path
        public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull Integer userId
    ) {
        final String fileUploadSubPath = "users" + separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    // This method is used to upload the file to the target path
    private String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath
    ) {
            // Get the final upload path
        final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        // Create the target folder if it does not exist
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            // If the folder creation fails, return null
            if (!folderCreated) {
                log.error("Failed to create the target folder for the file upload");
                return null;
            }
        }
        // Get the file extension
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
        // Get the target path
        Path targetPath = Paths.get(targetFilePath);
        // Write the file to the target path
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File uploaded successfully to:\t" +  targetFilePath);
            return targetFilePath;
        } catch (IOException exception) {
            log.error("Failed to write the file to the target path", exception);
        }
        return null;
    }

    // This method is used to get the file extension from the original file name
    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return null;
        }
        // Get the last index of the dot
        int lastDotIndex = originalFilename.lastIndexOf(".");
        // If the last dot index is -1, return null
        if (lastDotIndex == -1) {
            return null;
        }
        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }

}
