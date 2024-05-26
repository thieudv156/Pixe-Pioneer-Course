package vn.aptech.pixelpioneercourse.service;

<<<<<<< Updated upstream
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
=======
>>>>>>> Stashed changes
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Instructor;
import vn.aptech.pixelpioneercourse.repository.InstructorRepository;

import java.util.List;

@Service
<<<<<<< Updated upstream
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
=======
public class InstructorServiceImpl implements InstructorService{
    private InstructorRepository instructorRepository;


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
        return instructorRepository.findAll();
    }

    @Override
    public Instructor updateInstructor(int id, Instructor instructor) {
        Instructor existingInstructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        existingInstructor.setId(instructor.getId());
        existingInstructor.setUser(instructor.getUser());
        existingInstructor.setCourses(instructor.getCourses());
        existingInstructor.setInformation(instructor.getInformation());
        return instructorRepository.save(existingInstructor);
    }

    @Override
    public void deleteInstructor(int id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        instructorRepository.delete(instructor);
>>>>>>> Stashed changes
    }
}
