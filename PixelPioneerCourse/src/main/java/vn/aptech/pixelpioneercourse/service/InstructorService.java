package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Instructor;

import java.util.List;
<<<<<<< Updated upstream

public interface InstructorService {
    public List<Instructor> findAll();
    public Instructor findById(int id);
=======
import java.util.Optional;

public interface InstructorService {
    Instructor createInstructor(Instructor instructor);
    Instructor getInstructorById(int id);
    List<Instructor> getAllInstructors();
    void deleteInstructor(int id);
    Instructor updateInstructor(int id, Instructor instructor);
>>>>>>> Stashed changes
}
