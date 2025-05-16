package ray1024.imageservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ray1024.imageservice.model.response.ImageNameResponse;
import ray1024.imageservice.service.ImageService;

@RestController(value = "/api/images")
@AllArgsConstructor
public class ImageController {
    private ImageService imageService;

    @PostMapping
    public ImageNameResponse create(@RequestBody byte[] image) {
        return ImageNameResponse.builder()
                .imageName(imageService.saveImage(image))
                .build();
    }
}
