package vn.aptech.pixelpioneercourse.controller.api.image;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.entities.Image;
import vn.aptech.pixelpioneercourse.service.ImageService;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            Image result = imageService.uploadImageToFileSystem(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

   @GetMapping("/{imageName}")
    public ResponseEntity<?> downloadImage(@PathVariable("imageName") String imageName) {
        try {
            Pair<byte[], String> result = imageService.downloadImageFromFileSystem(imageName);
            byte[] imageData = result.getFirst();
            String contentType = result.getSecond();
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(contentType)).body(imageData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
