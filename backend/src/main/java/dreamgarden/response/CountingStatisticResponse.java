/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author vamilutinovic
 */
public class CountingStatisticResponse {
    
    @JsonProperty
    private String variableName;
    @JsonProperty
    private Integer variableCount;

    public CountingStatisticResponse() { }

    public CountingStatisticResponse(String variableName, Integer variableCount) {
        this.variableName = variableName;
        this.variableCount = variableCount;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Integer getVariableCount() {
        return variableCount;
    }

    public void setVariableCount(Integer variableCount) {
        this.variableCount = variableCount;
    }
    
}
