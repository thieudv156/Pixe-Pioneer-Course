package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Test;

public interface TestService {
    TestDto createTestDto(Integer testFormatId, Integer userId);
    Test submitTest(TestDto testDto, Integer userId);
}
