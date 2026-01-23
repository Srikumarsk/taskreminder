package com.task.taskreminder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String otp;

    private boolean verified = false;
    private String profileImage;


    // getters & setters
   public void setId(Long id) {
    this.id = id;
}
public Long getId() {
    return id;
}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public String getProfileImage() {
    return profileImage;
}

public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
}


}
