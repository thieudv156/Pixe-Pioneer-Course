package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.InstructorCreateDto;
import vn.aptech.pixelpioneercourse.entities.Instructor;
import vn.aptech.pixelpioneercourse.repository.InstructorRepository;


import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ModelMapper modelMapper;

//    private InstructorServiceImpl(InstructorRepository repository, ModelMapper modelMapper) {
//        this.instructorRepository = repository;
//        this.modelMapper = modelMapper;
//    }

    public List<Instructor> findAll() {
        try {
            return instructorRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("List of Instructor is null");
        }
    }

    public Instructor findById(int id) {
        try {
            return instructorRepository.findById(id).orElseThrow(() -> new RuntimeException("Instructor not found!"));
        } catch (Exception e) {
            throw new RuntimeException("Instructor is null");
        }
    }

    @Override
    public Instructor createInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor getInstructorById(int id) {
        return instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll().stream().toList();
    }


    @Override
    public Instructor updateInstructor(int id, InstructorCreateDto instructorCreateDto) {
        Instructor existingInstructor = instructorRepository.findById(id).orElseThrow(() -> new RuntimeException("Instructor not found"));

        existingInstructor.setId(modelMapper.map(instructorCreateDto, Instructor.class).getId());
        existingInstructor.setCourses(modelMapper.map(instructorCreateDto, Instructor.class).getCourses());
        existingInstructor.setUser(modelMapper.map(instructorCreateDto, Instructor.class).getUser());
        existingInstructor.setInformation(modelMapper.map(instructorCreateDto, Instructor.class).getInformation());
        return instructorRepository.save(existingInstructor);
    }

    @Override
    public void deleteInstructor(int id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        instructorRepository.delete(instructor);
    }
}
