package com.matemaker.campusmate.database;

import java.util.HashMap;
import java.util.Map;

public class UserDTO {
    public String uid = null;
    public String id = null;
    public String name = null;
    public String age = null;
    public String stu_num = null;
    public String gender = null;
    public String subject = null;

    public UserDTO(){}

    public UserDTO(String uid, String id, String name, String age, String stu_num, String gender,String subject){
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.age = age;
        this.stu_num = stu_num;
        this.gender = gender;
        this.subject = subject;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("uid",uid);
        dto.put("id",id);
        dto.put("name",name);
        dto.put("age",age);
        dto.put("stu_num",stu_num);
        dto.put("gender",gender);
        dto.put("subject", subject);
        return dto;
    }
}
