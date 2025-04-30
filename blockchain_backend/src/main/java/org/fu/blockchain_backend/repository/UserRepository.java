package org.fu.blockchain_backend.repository;

import org.fu.blockchain_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 可以在此添加自定义查询方法
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}