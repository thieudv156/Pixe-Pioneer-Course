package vn.aptech.pixelpioneercourse.service;

import java.util.List;

import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.TestFormat;

public interface TestFormatService {
    TestFormat findById(Integer id);
    TestFormat findByCourseId(Integer cID);
    TestFormat update(TestFormatCreateDto dto, Integer id);
}
