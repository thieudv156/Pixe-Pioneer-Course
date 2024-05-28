package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<User> findAll(){
        try{
            return userRepository.findAll();
        }
        catch (Exception e){
            throw new RuntimeException("List of User is null");
        }
    }

    public User findById(int id) {
        try {
            return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
        } catch (Exception e) {
            throw new RuntimeException("User is null");
        }
    }
}
