package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Instructor;
import vn.aptech.pixelpioneercourse.repository.InstructorRepository;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    private InstructorServiceImpl(InstructorRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<Instructor> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("List of Instructor is null");
        }
    }

    public Instructor findById(int id) {
        try {
            return repository.findById(id).orElseThrow(() -> new RuntimeException("Instructor not found!"));
        } catch (Exception e) {
            throw new RuntimeException("Instructor is null");
        }
    }
}
