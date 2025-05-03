package org.fu.blockchain_backend.controller;

import org.fu.blockchain_backend.common.Result;
import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.model.User;
import org.fu.blockchain_backend.service.UserDegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserDegreeController {

    @Autowired
    private UserDegreeService userDegreeService;

    //@GetMapping("/user/verify/{userId}")
    //public Result verifyDegree(@PathVariable String userId) {
    //    // 校验用户ID是否为空或非数字
    //    if (userId == null || userId.isEmpty() || !userId.matches("\\d+")) {
    //        return Result.error("无效的用户ID");
    //    }
    //    try {
    //
    //    }
    //}

    @GetMapping("/degree/{userId}")
    public Result getDegreeByUserId(@PathVariable String userId) {
        // 校验用户ID是否为空或非数字
        if (userId == null || userId.isEmpty() || !userId.matches("\\d+")) {
            return Result.error("无效的用户ID");
        }

        try {
            Long userIdLong = Long.parseLong(userId);
            User user = userDegreeService.findById(userIdLong);
            if (user == null) {
                return Result.error("用户不存在");
            }
            Degree degree = userDegreeService.findByIdCardNum(user.getIdCardNum());
            return Result.success(degree);
        } catch (Exception e) {
            return Result.error("获取学位信息失败: " + e.getMessage());
        }
    }

}
