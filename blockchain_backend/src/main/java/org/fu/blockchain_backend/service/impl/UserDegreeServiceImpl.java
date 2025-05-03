package org.fu.blockchain_backend.service.impl;

import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.model.User;
import org.fu.blockchain_backend.repository.DegreeRepository;
import org.fu.blockchain_backend.repository.UserRepository;
import org.fu.blockchain_backend.service.UserDegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDegreeServiceImpl implements UserDegreeService {

    @Autowired
    private DegreeRepository degreeRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id){
        return userRepository.findById(id).get();
    }

    @Override
    public Degree findByIdCardNum(String idCardNum) {
        return degreeRepository.findByIdCardNum(idCardNum);
    }

}
