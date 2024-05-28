package vn.aptech.pixelpioneercourse.controller.instructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.InstructorCreateDto;

import java.util.List;

@RestController
@RequestMapping("api/instructor")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;
    
    @Autowired
    private ModelMapper mapper;

    @PostMapping
    public ResponseEntity<Instructor> createInstructor(@RequestBody InstructorCreateDto instructorCreateDto) {
        Instructor createdInstructor = instructorService.createInstructor(mapper.map(instructorCreateDto, Instructor.class));
        return new ResponseEntity<>(createdInstructor, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable int id) {
        Instructor instructor = instructorService.getInstructorById(id);
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Instructor>> getAllInstructors() {
        List<Instructor> instructors = instructorService.getAllInstructors();
        return new ResponseEntity<>(instructors, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instructor> updateInstructor(@PathVariable int id, @RequestBody InstructorCreateDto instructorCreateDto) {
        Instructor updatedInstructor = instructorService.updateInstructor(id, instructorCreateDto);
        return new ResponseEntity<>(updatedInstructor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable int id) {
        instructorService.deleteInstructor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
