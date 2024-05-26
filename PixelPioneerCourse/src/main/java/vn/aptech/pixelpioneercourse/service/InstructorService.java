package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Instructor;

import java.util.List;
public interface InstructorService {
    public List<Instructor> findAll();
    public Instructor findById(int id);

    Instructor createInstructor(Instructor instructor);

    Instructor getInstructorById(int id);

    List<Instructor> getAllInstructors();

    Instructor updateInstructor(int id, Instructor instructor);

    void deleteInstructor(int id);
}
