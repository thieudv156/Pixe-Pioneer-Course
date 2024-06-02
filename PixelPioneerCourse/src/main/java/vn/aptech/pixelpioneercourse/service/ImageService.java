package vn.aptech.pixelpioneercourse.service;

import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.entities.Image;

public interface ImageService {
    Image findByImageName(String name);
    String uploadImageToFileSystem(MultipartFile file);
    Pair<byte[], String> downloadImageFromFileSystem(String fileName);
}
