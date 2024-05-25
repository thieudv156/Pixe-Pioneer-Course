package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Instructor;

import java.util.List;

public interface InstructorService {
    public List<Instructor> findAll();
    public Instructor findById(int id);
}
