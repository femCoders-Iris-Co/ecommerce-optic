package com.irisandco.ecommerce_optic.cloudinary;

import java.util.Map;

public class CloudinaryMapper {
    public static CloudinaryResponse fromMap(Map<?, ?> result) {
        return CloudinaryResponse.builder()
                .publicId((String) result.get("public_id"))
                .url((String) result.get("secure_url"))
                .build();
    }
}
