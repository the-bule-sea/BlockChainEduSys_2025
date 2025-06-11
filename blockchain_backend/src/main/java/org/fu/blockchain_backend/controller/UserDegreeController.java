package org.fu.blockchain_backend.controller;

import org.fu.blockchain_backend.blockchain.BlockchainService;
import org.fu.blockchain_backend.common.Result;
import org.fu.blockchain_backend.dto.DegreeVerifyResultDTO;
import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.model.User;
import org.fu.blockchain_backend.service.DegreeService;
import org.fu.blockchain_backend.service.UserDegreeService;
import org.fu.blockchain_backend.util.DegreeHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserDegreeController {

    @Autowired
    private UserDegreeService userDegreeService;

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping("/verify/{userId}")
    public Result verifyDegree(@PathVariable String userId) {
        List<String> problem = new ArrayList<>();
        User user = userDegreeService.findById(Long.parseLong(userId));
        // 校验用户ID是否为空或非数字
        if (userId == null || userId.isEmpty() || !userId.matches("\\d+")) {
            return Result.error("无效的用户ID");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String idCardNum = user.getIdCardNum();

            Degree localDegree = userDegreeService.findByIdCardNum(idCardNum);
            Degree blockchainDegree = blockchainService.getDegreeByIdCard(idCardNum); // 已处理空列表的逻辑

            String localHash = DegreeHashUtil.calculateDegreeHash(
                    localDegree.getName(), localDegree.getIdCardNum(),
                    localDegree.getUniversity(), localDegree.getMajor(),
                    localDegree.getDegreeLevel(), localDegree.getGraduationDate().format(formatter)
            );
            String blockchainHash = DegreeHashUtil.calculateDegreeHash(
                    blockchainDegree.getName(), blockchainDegree.getIdCardNum(),
                    blockchainDegree.getUniversity(), blockchainDegree.getMajor(),
                    blockchainDegree.getDegreeLevel(), blockchainDegree.getGraduationDate().format(formatter)
            );

            DegreeVerifyResultDTO result = new DegreeVerifyResultDTO();
            result.setDegree(localDegree);
            result.setLocalHash(localHash);
            result.setBlockchainHash(blockchainHash);
            result.setValid(localHash.equals(blockchainHash));

            return Result.success(result);

        } catch (Exception e) {

            return Result.error("验证学位信息失败: " + e.getMessage());
        }
    }

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
