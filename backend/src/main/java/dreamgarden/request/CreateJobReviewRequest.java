/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dreamgarden.request;

/**
 *
 * @author vamilutinovic
 */
public class CreateJobReviewRequest {
  
    private String comment;
    private Integer grade;
    private Integer jobId;

    public CreateJobReviewRequest() { }
    
    public CreateJobReviewRequest(String comment, Integer grade, Integer jobId) {
        this.comment = comment;
        this.grade = grade;
        this.jobId = jobId;
    }
    
    public boolean checkCreateJobReviewRequest(){
        return this.comment != null
            && this.grade != null
            && this.jobId != null;
    }
    

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }


}
