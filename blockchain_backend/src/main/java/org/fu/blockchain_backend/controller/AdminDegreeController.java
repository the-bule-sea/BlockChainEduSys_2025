package org.fu.blockchain_backend.controller;

import org.fu.blockchain_backend.common.Result;
import org.fu.blockchain_backend.dto.DegreeDTO;
import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/degree")
public class AdminDegreeController {
    @Autowired
    private DegreeService degreeService;

    @PostMapping("/page")
    public Result findPage(@RequestBody DegreeDTO degreeDTO) {
        //System.out.println(degreeDTO.toString());
        Page<Degree> page = degreeService.findPage(degreeDTO);
        return Result.success(page);
    }

    @PostMapping
    public Result savaOrUpdate(@RequestBody @Valid Degree degree) {
        Degree degree1 = degreeService.savaOrUpdate(degree);
        if (degree1 == null) {
            return Result.error("上链失败");
        }
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        try {
            degreeService.delete(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.toString());
        }
    }

    @GetMapping("/verifyAll")
    public Result verifyAllDegrees() {
        List<String> problems = degreeService.verifyAllDegrees();
        if (problems.isEmpty()) {
            return Result.success();
        } else {
            return Result.error("以下记录存在不一致", problems);
        }
    }

}
