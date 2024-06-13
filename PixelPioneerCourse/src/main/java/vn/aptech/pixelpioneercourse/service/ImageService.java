package vn.aptech.pixelpioneercourse.service;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.entities.Image;

public interface ImageService {
    Image findByImageName(String name);
    Image uploadImageToFileSystem(MultipartFile file);
    Pair<byte[], String> downloadImageFromFileSystem(String fileName);
    String uploadImage(MultipartFile file);
}
