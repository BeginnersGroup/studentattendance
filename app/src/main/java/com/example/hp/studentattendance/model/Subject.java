package com.example.hp.studentattendance.model;

/**
 * Created by HP on 06/04/2019.
 */

public class Subject {
    String subName;
    int semster;
    int branchId;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getSemster() {
        return semster;
    }

    public void setSemster(int semster) {
        this.semster = semster;
    }
}
