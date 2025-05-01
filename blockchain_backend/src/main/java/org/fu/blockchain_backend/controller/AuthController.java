package org.fu.blockchain_backend.controller;

import org.fu.blockchain_backend.common.Result;
import org.fu.blockchain_backend.model.User;
import org.fu.blockchain_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        User user1 = userRepository.findByEmailAndPassword(email, password);

        if (user1 != null) {
            return Result.success();
        } else {
            return Result.error("邮箱或密码错误");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 1. 校验用户输入
        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
            return Result.error("请输入邮箱与密码");
        }

        // 2. 检查邮箱是否已存在
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return Result.error("邮箱已存在");
        }

        // 3. 保存用户到数据库
        userRepository.save(user);

        // 4. 返回成功结果
        return Result.success();

    }
}