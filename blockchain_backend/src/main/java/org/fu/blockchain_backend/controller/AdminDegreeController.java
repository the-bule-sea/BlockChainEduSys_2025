package org.fu.blockchain_backend.controller;

import org.fu.blockchain_backend.common.Result;
import org.fu.blockchain_backend.dto.DegreeDTO;
import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin/degree")
public class AdminDegreeController {
    @Autowired
    private DegreeService degreeService;

    @PostMapping("/page")
    public Result findPage(@RequestBody DegreeDTO degreeDTO){
        System.out.println(degreeDTO.toString());
        Page<Degree> page = degreeService.findPage(degreeDTO);
        return Result.success(page);
    }

}
