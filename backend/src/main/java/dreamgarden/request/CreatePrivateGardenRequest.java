/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

import java.math.BigDecimal;

/**
 *
 * @author vamilutinovic
 */
public class CreatePrivateGardenRequest {
    private BigDecimal poolSize;
    private Integer numberOfPools;
    private BigDecimal grassSize;
    private BigDecimal pavedSize;

    public CreatePrivateGardenRequest() {
    }

    public CreatePrivateGardenRequest(BigDecimal poolSize, Integer numberOfPools, BigDecimal grassSize, BigDecimal pavedSize) {
        this.poolSize = poolSize;
        this.numberOfPools = numberOfPools;
        this.grassSize = grassSize;
        this.pavedSize = pavedSize;
    }

    public BigDecimal getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(BigDecimal poolSize) {
        this.poolSize = poolSize;
    }

    public Integer getNumberOfPools() {
        return numberOfPools;
    }

    public void setNumberOfPools(Integer numberOfPools) {
        this.numberOfPools = numberOfPools;
    }

    public BigDecimal getGrassSize() {
        return grassSize;
    }

    public void setGrassSize(BigDecimal grassSize) {
        this.grassSize = grassSize;
    }

    public BigDecimal getPavedSize() {
        return pavedSize;
    }

    public void setPavedSize(BigDecimal pavedSize) {
        this.pavedSize = pavedSize;
    }
    
}
