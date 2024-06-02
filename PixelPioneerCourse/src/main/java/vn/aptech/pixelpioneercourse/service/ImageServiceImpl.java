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

    private static final String FOLDER_PATH = "/Users/tu4n/End_term_project/Pixe-Pioneer-Course/PixelPioneerCourse/src/main/resources/templates/static/public/images/upload_images";
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

   public String uploadImageToFileSystem(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Original filename cannot be null or empty");
        }

        String filePath = FOLDER_PATH + "/" + originalFilename;
        Image img = Image.builder()
                .imageName(originalFilename)
                .imageType(file.getContentType())
                .imageUrl(filePath)
                .build();

        imageRepository.save(img);

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return originalFilename;
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
}