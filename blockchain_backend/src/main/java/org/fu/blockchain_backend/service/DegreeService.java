package org.fu.blockchain_backend.service;

import org.fu.blockchain_backend.dto.DegreeDTO;
import org.fu.blockchain_backend.model.Degree;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DegreeService {
    Page<Degree> findPage(DegreeDTO degreeDTO);
    Degree savaOrUpdate(Degree degree);
    void delete(String id);
    List<String> verifyAllDegrees();
}
