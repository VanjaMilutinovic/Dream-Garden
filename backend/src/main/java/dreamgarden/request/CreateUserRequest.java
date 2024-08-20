package dreamgarden.request;


/**
 *
 * @author vamilutinovic
 */
public class CreateUserRequest {
    
    private String username;
    private String hashedPassword;
    private String name;
    private String lastname;
    private Character gender;
    private String address;
    private String contactNumber;
    private String email;
    private String creditCardNumber;
    private Integer userTypeId;
    private Integer photoId;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String username, String hashedPassword, String name, String lastname, Character gender, String address, String contactNumber, String email, String creditCardNumber, Integer userTypeId, Integer photoId) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.lastname = lastname;
        this.gender = gender;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.creditCardNumber = creditCardNumber;
        this.userTypeId = userTypeId;
        this.photoId = photoId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
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

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
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

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

}
