package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public User findById(int id){
        try {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        }
        catch (Exception e){
            throw new RuntimeException("Category is null");
        }
    }
}
