package com.mocrowu.cxd.prototype;

public class Resume implements Cloneable {
    private String name;
    private String birthday;
    private String sex;
    private String school;
    private String timeArea;
    private String company;

    public Resume(String name){
        this.name = name;
    }

    public void setPersonInfo(String birthday,String sex,String school){
        this.birthday = birthday;
        this.sex = sex;
        this.school = school;
    }

    public void setWorkExperience(String timeArea,String company){
        this.timeArea = timeArea;
        this.company = company;
    }
    public void display(){
        System.out.println("姓名：" + name);
        System.out.println("生日:" + birthday + ",性别:" + sex + ",毕业学校：" + school);
        System.out.println("工作年限:" + timeArea + ",公司:" + company);
    }
    public Resume clone(){
        Resume resume = null;
        try{
            resume = (Resume)super.clone();
        }catch(Exception e){
            e.printStackTrace();
        }
        return resume;
    }
}
