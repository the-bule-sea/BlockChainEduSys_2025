package org.fu.blockchain_backend.controller;

import org.fu.blockchain_backend.common.Result;
import org.fu.blockchain_backend.dto.LoginDTO;
import org.fu.blockchain_backend.model.Role;
import org.fu.blockchain_backend.model.User;
import org.fu.blockchain_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        String identityType = loginDTO.getIdentityType();
        String password = loginDTO.getPassword();
        // 判断是管理员登录还是普通用户登录
        if ("ADMIN".equalsIgnoreCase(identityType)) {
            String email = loginDTO.getEmail();
            if (email == null || password == null) {
                return Result.error("管理员登录需提供邮箱和密码");
            }
            User adminUser = userRepository.findByEmailAndPassword(email, password);
            if (adminUser != null && adminUser.getRole() == Role.ADMIN) {
                return Result.success(new User(adminUser.getRole()));
            } else {
                return Result.error("管理员邮箱或密码错误");
            }
        } else if ("USER".equalsIgnoreCase(identityType)) {
            String idCard = loginDTO.getIdCardNum();
            if (idCard == null || password == null) {
                return Result.error("用户登录需提供身份证号和密码");
            }
            User normalUser = userRepository.findByIdCardNumAndPassword(idCard, password);
            if (normalUser != null && normalUser.getRole() == Role.USER) {
                return Result.success(normalUser);
            } else {
                return Result.error("身份证号或密码错误");
            }
        } else {
            return Result.error("缺少必要的登录凭证");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        // 1. 校验用户输入
        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null || user.getIdCardNum() == null) {
            return Result.error("请输入完整信息");
        }

        // 2. 检查邮箱是否已存在
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return Result.error("邮箱已存在");
        }

        user.setRole(Role.USER);

        // 3. 保存用户到数据库
        userRepository.save(user);

        // 4. 返回成功结果
        return Result.success();

    }
}