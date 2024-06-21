package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.TestDto;

public interface TestService {
    TestDto createTestDto(Integer testFormatId, Integer userId);
}
