package com.college.skillbridge.dtos;

public class AdminRegistrationRequest {
    private String adminName;
    private String adminEmail;
    private String password;
    private String phoneNumber;
    private String roleTitle;
    private String collegeName;
    private String collegeDomain;
    private String collegeWebsite;
    private String collegeContactEmail;
    private String collegeContactPhone;
    private String collegeAddress;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeDomain() {
        return collegeDomain;
    }

    public void setCollegeDomain(String collegeDomain) {
        this.collegeDomain = collegeDomain;
    }

    public String getCollegeWebsite() {
        return collegeWebsite;
    }

    public void setCollegeWebsite(String collegeWebsite) {
        this.collegeWebsite = collegeWebsite;
    }

    public String getCollegeContactEmail() {
        return collegeContactEmail;
    }

    public void setCollegeContactEmail(String collegeContactEmail) {
        this.collegeContactEmail = collegeContactEmail;
    }

    public String getCollegeContactPhone() {
        return collegeContactPhone;
    }

    public void setCollegeContactPhone(String collegeContactPhone) {
        this.collegeContactPhone = collegeContactPhone;
    }

    public String getCollegeAddress() {
        return collegeAddress;
    }

    public void setCollegeAddress(String collegeAddress) {
        this.collegeAddress = collegeAddress;
    }
}

