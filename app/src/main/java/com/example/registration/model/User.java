package com.example.registration.model;

public class User {

    private Integer reg_id;
    private String reg_name;
    private String reg_mobile;
    private String reg_email;
    private String reg_password;

    public Integer getReg_id() {
        return reg_id;
    }

    public void setReg_id(Integer reg_id) {
        this.reg_id = reg_id;
    }

    public String getReg_name() {
        return reg_name;
    }

    public void setReg_name(String reg_name) {
        this.reg_name = reg_name;
    }

    public String getReg_mobile() {
        return reg_mobile;
    }

    public void setReg_mobile(String reg_mobile) {
        this.reg_mobile = reg_mobile;
    }

    public String getReg_email() {
        return reg_email;
    }

    public void setReg_email(String reg_email) {
        this.reg_email = reg_email;
    }

    public String getReg_password() {
        return reg_password;
    }

    public void setReg_password(String reg_password) {
        this.reg_password = reg_password;
    }

    public User(Integer reg_id, String reg_name, String reg_mobile, String reg_email, String reg_password) {

        this.reg_id = reg_id;
        this.reg_name = reg_name;
        this.reg_mobile = reg_mobile;
        this.reg_email = reg_email;
        this.reg_password = reg_password;



    }

    public User(Integer reg_id, String reg_name, String reg_mobile, String reg_email) {

        this.reg_id = reg_id;
        this.reg_name = reg_name;
        this.reg_mobile = reg_mobile;
        this.reg_email = reg_email;

    }
}
