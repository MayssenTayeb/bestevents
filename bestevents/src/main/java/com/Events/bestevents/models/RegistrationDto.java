package com.Events.bestevents.models;

import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.*;


public class RegistrationDto {

        @NotEmpty(message="the  Name is required")
	    private String Name;

	    @NotEmpty(message="the email is required")
	    private String Email;

	    @NotEmpty(message="the CIN  is required")
	    
	    private String Cin;
	    @NotEmpty(message="the PaymentType  is required")
	    private String PaymentType;
	    
	   
	    @Size(max=8,message="the tel cannot exceed more than 8 characters")
	    private String tel; 
	    
	    private MultipartFile imageFile;
        


    /**
     * @return String return the Name
     */
    public String getName() {
        return Name;
    }

    /**
     * @param Name the Name to set
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * @return String return the Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param Email the Email to set
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * @return String return the Cin
     */
    public String getCin() {
        return Cin;
    }

    /**
     * @param Cin the Cin to set
     */
    public void setCin(String Cin) {
        this.Cin = Cin;
    }

    /**
     * @return String return the PaymentType
     */
    public String getPaymentType() {
        return PaymentType;
    }

    /**
     * @param PaymentType the PaymentType to set
     */
    public void setPaymentType(String PaymentType) {
        this.PaymentType = PaymentType;
    }

    /**
     * @return String return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return MultipartFile return the imageFile
     */
    public MultipartFile getImageFile() {
        return imageFile;
    }

    /**
     * @param imageFile the imageFile to set
     */
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}
