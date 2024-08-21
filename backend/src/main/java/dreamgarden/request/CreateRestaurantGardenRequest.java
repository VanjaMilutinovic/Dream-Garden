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
public class CreateRestaurantGardenRequest {
    private BigDecimal fountainSize;
    private Integer numberOfFountains;
    private BigDecimal grassSize;
    private Integer numberOfTables;
    private Integer numberOfSeats;

    public CreateRestaurantGardenRequest() {
    }

    public CreateRestaurantGardenRequest(BigDecimal fountainSize, Integer numberOfFountains, BigDecimal grassSize, Integer numberOfTables, Integer numberOfSeats) {
        this.fountainSize = fountainSize;
        this.numberOfFountains = numberOfFountains;
        this.grassSize = grassSize;
        this.numberOfTables = numberOfTables;
        this.numberOfSeats = numberOfSeats;
    }

    public BigDecimal getFountainSize() {
        return fountainSize;
    }

    public void setFountainSize(BigDecimal fountainSize) {
        this.fountainSize = fountainSize;
    }

    public Integer getNumberOfFountains() {
        return numberOfFountains;
    }

    public void setNumberOfFountains(Integer numberOfFountains) {
        this.numberOfFountains = numberOfFountains;
    }

    public BigDecimal getGrassSize() {
        return grassSize;
    }

    public void setGrassSize(BigDecimal grassSize) {
        this.grassSize = grassSize;
    }

    public Integer getNumberOfTables() {
        return numberOfTables;
    }

    public void setNumberOfTables(Integer numberOfTables) {
        this.numberOfTables = numberOfTables;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
    
}
