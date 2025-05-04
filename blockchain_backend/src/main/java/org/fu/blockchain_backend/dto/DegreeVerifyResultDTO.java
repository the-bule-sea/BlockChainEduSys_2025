package org.fu.blockchain_backend.dto;

import org.fu.blockchain_backend.model.Degree;

public class DegreeVerifyResultDTO {
    private Degree degree;
    private String localHash;
    private String blockchainHash;
    private Boolean valid;

    public Degree getDegree() {
        return degree;
    }

    public String getLocalHash() {
        return localHash;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public void setLocalHash(String localHash) {
        this.localHash = localHash;
    }

    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
