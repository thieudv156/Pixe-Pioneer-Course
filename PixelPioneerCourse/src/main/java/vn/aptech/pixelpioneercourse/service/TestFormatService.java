package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.TestFormat;

public interface TestFormatService {
    TestFormat save(TestFormatCreateDto dto);
    TestFormat findById(Integer id);
    TestFormat update(TestFormatCreateDto dto, Integer id);
    boolean deleteById(Integer id);
}
