package com.example.stufun.Model;

public class ClassRoomModel {
    String classname,classcode,classsubject,datecreated,classimage,teachername;
    Integer classcolorcode;

    public ClassRoomModel() {
    }

    public ClassRoomModel(String classname, String classcode, String classsubject, String datecreated, String classimage, Integer classcolorcode, String teachername) {
        this.classname = classname;
        this.classcode = classcode;
        this.classsubject = classsubject;
        this.datecreated = datecreated;
        this.classimage = classimage;
        this.classcolorcode = classcolorcode;
        this.teachername = teachername;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasscode() {
        return classcode;
    }

    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }

    public String getClasssubject() {
        return classsubject;
    }

    public void setClasssubject(String classsubject) {
        this.classsubject = classsubject;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getClassimage() {
        return classimage;
    }

    public void setClassimage(String classimage) {
        this.classimage = classimage;
    }

    public int getClasscolorcode() {
        return classcolorcode;
    }

    public void setClasscolorcode(Integer classcolorcode) {
        this.classcolorcode = classcolorcode;
    }
}
