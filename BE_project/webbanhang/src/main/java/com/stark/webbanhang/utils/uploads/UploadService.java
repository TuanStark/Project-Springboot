package com.stark.webbanhang.utils.uploads;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class UploadService {

    @Value("${path-upload}")
    private String uploadDir;
    private Path root;

    // tạo là folder root đó khi nó không tồn tại
    public void inIt() {
        try {
            root = Paths.get(uploadDir);
            if (Files.notExists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            System.out.println("Error creating folder root: " + e.getMessage());
        }
    }
    public boolean saveFile(MultipartFile file) {
        try {
            inIt();
            // Lưu file vào đường dẫn và ghi đè nếu file đã tồn tại
            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
            return false;
        }
    }



    // Load a file as a resource for downloading
    public Resource loadFile(String fileName) {
        try {
            inIt();
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + fileName, e);
        }
    }

    // Delete a file given the file path
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (NoSuchFileException e) {
            System.err.println("File not found: " + filePath);
            return false;
        } catch (DirectoryNotEmptyException e) {
            System.err.println("Directory is not empty: " + filePath);
            return false;
        } catch (IOException e) {
            System.err.println("I/O error occurred when deleting the file: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error occurred: " + e.getMessage());
            return false;
        }
    }

    // Helper method to validate file types
    private boolean isSupportedContentType(String contentType) {
        // Add validation for the file types allowed (images in this case)
        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

}
