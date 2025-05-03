package org.fu.blockchain_backend.repository;

import org.fu.blockchain_backend.model.Degree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DegreeRepository extends JpaRepository<Degree, Long>, JpaSpecificationExecutor<Degree> {
    Degree findByIdCardNum(String idCardNum);
}
