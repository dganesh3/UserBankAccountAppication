package com.user.CustomerProfile.Service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class FileStorageService 
{

    private final Path baseUploadDir = Paths.get("uploads");

    public String uploadCompressedImage(MultipartFile file, String oldPath) {
        if (file == null || file.isEmpty()) return null;

        try {
            //  Delete old image if exists
            deleteFileIfExists(oldPath);

            //  Generate unique filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path fullPath = baseUploadDir.resolve(filename);

            // Ensure directory exists
            Files.createDirectories(baseUploadDir);

            //  Compress & Save image
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            File compressedFile = fullPath.toFile();

            Thumbnails.of(originalImage)
                    .scale(1.0)
                    .outputQuality(0.5f) // 50% quality (can adjust)
                    .toFile(compressedFile);

            //  Return relative path for DB
            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload or compress image", e);
        }
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(baseUploadDir); 
            
            System.out.println(" Upload folder ready: " + baseUploadDir.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(" Could not create upload directory!", e);
        }
    }
    
    
    public void deleteFileIfExists(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return;

        try {
            // Use relative path under project root
            Path fullPath = Paths.get("uploads" + File.separator + Paths.get(relativePath).getFileName());
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
                System.out.println(" Deleted file: " + fullPath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println(" Failed to delete file: " + relativePath + " - " + e.getMessage());
        }
    }


}
