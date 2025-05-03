package org.fu.blockchain_backend.service.impl;

import org.fu.blockchain_backend.dto.DegreeDTO;
import org.fu.blockchain_backend.model.Degree;
import org.fu.blockchain_backend.repository.DegreeRepository;
import org.fu.blockchain_backend.service.DegreeService;
import org.fu.blockchain_backend.blockchain.BlockchainService;
import org.fu.blockchain_backend.util.DegreeHashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
public class DegreeServiceImpl implements DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    @Autowired
    private BlockchainService blockchainService;

    /**
     * 根据DTO中的条件分页查询Degree实体
     * 此方法覆盖了默认的查询方法，以实现根据特定条件的分页查询
     *
     * @param dto DegreeDTO对象，包含查询条件和分页信息
     * @return 返回一个Page对象，包含查询结果和分页信息
     */
    @Override
    public Page<Degree> findPage(DegreeDTO dto) {
        System.out.println(dto.toString());
        // 创建一个Specification对象，用于动态生成查询条件
        Specification<Degree> spec = (root, query, cb) -> {
            // 初始化一个Predicate列表，用于存储所有查询条件
            List<Predicate> predicates = new ArrayList<>();

            // 如果DTO中的name字段不为空，则添加名称模糊查询条件
            if (dto.getName() != null && !dto.getName().isEmpty()) {
                predicates.add(cb.like(root.get("name"), "%" + dto.getName() + "%"));
            }

            // 如果DTO中的idCardNum字段不为空，则添加身份证号模糊查询条件
            if (dto.getIdCardNum() != null && !dto.getIdCardNum().isEmpty()) {
                predicates.add(cb.like(root.get("idCardNum"), "%" + dto.getIdCardNum() + "%"));
            }

            // 将所有条件组合为一个AND查询条件，并返回
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 创建一个Pageable对象，用于指定分页信息和排序方式
        // 注意：页码从0开始，因此需要将dto中的pageNum减1
        Pageable pageable = PageRequest.of(dto.getPageNum() - 1, dto.getPageSize(), Sort.by("graduationDate").descending());

        // 使用Specification和Pageable对象进行查询，并返回结果
        return degreeRepository.findAll(spec, pageable);
    }

    @Override
    public Degree savaOrUpdate(Degree degree){
        String timestamp = String.valueOf(System.currentTimeMillis());
        String hash = blockchainService.uploadDegreeToBlockchain(degree, timestamp);
        if(!hash.isEmpty()){
            degree.setTimestamp(timestamp);
            degree.setTxHash(hash);
            return degreeRepository.save(degree);
        }else {
            return null;
        }
    }

    @Override
    public void delete(String id) {
        degreeRepository.deleteById(Long.parseLong(id));
    }

    @Override
    public List<String> verifyAllDegrees() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Degree> localDegrees = degreeRepository.findAll();
        List<String> mismatches = new ArrayList<>();

        for (Degree local : localDegrees) {
            Degree chainDegree = blockchainService.getDegreeByIdCard(local.getIdCardNum());
            if (chainDegree == null) {
                mismatches.add("链上缺失身份证：" + local.getIdCardNum() + ", 请核实");
                continue;
            }
            String localHash = DegreeHashUtil.calculateDegreeHash(
                    local.getName(), local.getIdCardNum(), local.getUniversity(),
                    local.getMajor(), local.getDegreeLevel(), local.getGraduationDate().toString()
            );

            String chainHash = DegreeHashUtil.calculateDegreeHash(
                    chainDegree.getName(), chainDegree.getIdCardNum(), chainDegree.getUniversity(),
                    chainDegree.getMajor(), chainDegree.getDegreeLevel(), chainDegree.getGraduationDate().format(formatter)
            );

            if (!localHash.equals(chainHash)) {
                mismatches.add("不一致：身份证 " + local.getIdCardNum());
            }
        }
        return mismatches;
    }
}
