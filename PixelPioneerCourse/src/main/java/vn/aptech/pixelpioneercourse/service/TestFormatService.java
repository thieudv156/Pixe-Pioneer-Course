package vn.aptech.pixelpioneercourse.service;

import java.util.List;

import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.TestFormat;

public interface TestFormatService {
    TestFormat save(TestFormatCreateDto dto);
    TestFormat findById(Integer id);
    List<TestFormat> findByCourseId(Integer cID);
    TestFormat update(TestFormatCreateDto dto, Integer id);
    boolean deleteById(Integer id);
}
