package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.repository.TestRepository;

@Service
public class TestServiceImpl implements TestService{
    private final TestRepository testRepository;
    private final ModelMapper modelMapper;

    public TestServiceImpl(TestRepository testRepository, ModelMapper modelMapper) {
        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
    }


}
