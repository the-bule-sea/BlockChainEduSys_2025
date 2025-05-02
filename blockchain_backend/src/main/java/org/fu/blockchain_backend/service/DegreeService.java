package org.fu.blockchain_backend.service;

import org.fu.blockchain_backend.dto.DegreeDTO;
import org.fu.blockchain_backend.model.Degree;
import org.springframework.data.domain.Page;

public interface DegreeService {
    Page<Degree> findPage(DegreeDTO degreeDTO);
}
