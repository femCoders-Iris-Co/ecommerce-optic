package com.irisandco.ecommerce_optic.cloudinary;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CloudinaryResponse {
    private String publicId;
    private String url;
}
