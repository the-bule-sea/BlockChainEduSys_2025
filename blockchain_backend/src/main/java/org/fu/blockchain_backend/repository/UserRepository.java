package org.fu.blockchain_backend.repository;

import org.fu.blockchain_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 可以在此添加自定义查询方法
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    User findByIdCardNumAndPassword(String idCardNum, String password);

    Optional<User> findById(Long id);
}