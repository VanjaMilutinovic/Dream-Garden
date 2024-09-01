/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

/**
 *
 * @author vamilutinovic
 */
public class UpdateUserRequest {
    
    private Integer userId;
    private String name;
    private String lastname;
    private String address;
    private String contactNumber;
    private String email;
    private String creditCardNumber;
    private String base64;

    public UpdateUserRequest() { }

    public UpdateUserRequest(Integer userId, String name, String lastname, String address, String contactNumber, String email, String creditCardNumber, String photoPath) {
        this.userId = userId;
        this.name = name;
        this.lastname = lastname;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.base64 = photoPath;
    }
    
    public boolean checkUpdateUserRequest() {
        return userId != null && 
               name != null &&
               lastname != null &&
               address != null &&
               contactNumber != null &&
               email != null &&
               creditCardNumber != null &&
               base64 != null;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
    
}
