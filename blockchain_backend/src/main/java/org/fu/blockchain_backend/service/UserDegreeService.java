package org.fu.blockchain_backend.service;

import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.model.User;

public interface UserDegreeService {
    // 根据User表中的id查idCardNum
    User findById(Long id);

    // 根据身份证号查询学位信息
    Degree findByIdCardNum(String idCardNum);
}
