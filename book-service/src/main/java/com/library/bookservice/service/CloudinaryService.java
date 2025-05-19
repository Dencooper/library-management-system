package com.library.bookservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        return uploadResult.get("secure_url").toString();

        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("folder", "bookImages");
        uploadOptions.put("resource_type", "auto");

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new IOException("Failed to upload image to Cloudinary: " + e.getMessage(), e);
        }
    }
}
