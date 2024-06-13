package vn.aptech.pixelpioneercourse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.entities.Image;
import vn.aptech.pixelpioneercourse.repository.ImageRepository;
import org.springframework.data.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final Path FOLDER_PATH = Path.of(Paths.get(System.getProperty("user.dir")) + "/PixelPioneerCourse/src/main/resources/static/public/images/content_images");
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image findByImageName(String name) {
        try {
            return imageRepository.findByImageName(name).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Image is null");
        }
    }

    public Image uploadImageToFileSystem(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Original filename cannot be null or empty");
        }

        String filePath = FOLDER_PATH + "/" + System.currentTimeMillis()+"_"+originalFilename;

        Image img = Image.builder()
                .imageName(System.currentTimeMillis()+"_"+originalFilename )
                .imageType(file.getContentType())
                .imageUrl(filePath)
                .build();

        imageRepository.save(img);

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return img;
    }



    public Pair<byte[], String> downloadImageFromFileSystem(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        Optional<Image> imageOptional = imageRepository.findByImageName(fileName);
        if (imageOptional.isEmpty()) {
            throw new RuntimeException("Image not found: " + fileName);
        }

        String filePath = imageOptional.get().getImageUrl();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new RuntimeException("File does not exist: " + filePath);
        }

        try {
            byte[] imageData = Files.readAllBytes(path);
            String contentType = imageOptional.get().getImageType();
            return Pair.of(imageData, contentType);
        } catch (IOException e) {
            throw new RuntimeException("Could not read the file. Error: " + e.getMessage());
        }
    }

    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Original filename cannot be null or empty");
        }

        String filePath = FOLDER_PATH + "/" + System.currentTimeMillis()+"_"+originalFilename;

        Image img = Image.builder()
                .imageName(System.currentTimeMillis()+"_"+originalFilename )
                .imageType(file.getContentType())
                .imageUrl(filePath)
                .build();

        imageRepository.save(img);

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        String fileUrl = "http://localhost:8080/api/image/" + img.getImageName();
        return "{\"uploaded\": true, \"url\": \"" + fileUrl + "\"}";
    }
}