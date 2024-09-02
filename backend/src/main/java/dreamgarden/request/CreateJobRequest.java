/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author vamilutinovic
 */
public class CreateJobRequest {
    private Date startDateTime;
    private BigDecimal gardenSize;
    private String description;
    private CreatePrivateGardenRequest privateGarden;
    private CreateRestaurantGardenRequest restaurantGarden;
    private Integer userId;
    private Integer companyId;
    private Integer gardenTypeId;
    private String canvas;

    public CreateJobRequest() {
    }

    public CreateJobRequest(String canvas, Date startDateTime, BigDecimal gardenSize, String description, CreatePrivateGardenRequest privateGarden, CreateRestaurantGardenRequest restaurantGarden, Integer userId, Integer companyId, Integer gardenTypeId) {
        this.startDateTime = startDateTime;
        this.gardenSize = gardenSize;
        this.description = description;
        this.privateGarden = privateGarden;
        this.restaurantGarden = restaurantGarden;
        this.userId = userId;
        this.companyId = companyId;
        this.gardenTypeId = gardenTypeId;
        this.canvas = canvas;
    }

    public boolean checkCreateJobRequest() {
        return this.gardenSize!= null
            && this.startDateTime != null
            && this.description != null
            && this.userId!= null
            && this.companyId != null
            && this.gardenTypeId != null
            && ((this.gardenTypeId == 1 && this.privateGarden != null)
            || (this.gardenTypeId == 2 && this.restaurantGarden!= null));
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public BigDecimal getGardenSize() {
        return gardenSize;
    }

    public void setGardenSize(BigDecimal gardenSize) {
        this.gardenSize = gardenSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreatePrivateGardenRequest getPrivateGarden() {
        return privateGarden;
    }

    public void setPrivateGarden(CreatePrivateGardenRequest privateGarden) {
        this.privateGarden = privateGarden;
    }

    public CreateRestaurantGardenRequest getRestaurantGarden() {
        return restaurantGarden;
    }

    public void setRestaurantGarden(CreateRestaurantGardenRequest restaurantGarden) {
        this.restaurantGarden = restaurantGarden;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getGardenTypeId() {
        return gardenTypeId;
    }

    public void setGardenTypeId(Integer gardenTypeId) {
        this.gardenTypeId = gardenTypeId;
    }
    
}
