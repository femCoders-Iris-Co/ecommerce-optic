package com.irisandco.ecommerce_optic.cloudinary;


import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName) {
        try {
            final Map result = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id", "optics/product" + fileName));
            return CloudinaryMapper.fromMap(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file");
        }
    }
}