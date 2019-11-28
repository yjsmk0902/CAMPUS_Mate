package com.matemaker.campusmate.database;

import java.util.HashMap;
import java.util.Map;

public class MoimDTO {
    public long timestamp;
    public String title = null;
    public String subtitle = null;
    public String image = null;
    public String uid = null;
    public String number = null;
    public String age_max = null;
    public String age_min = null;
    public String category = null;
    public MoimDTO(){}

    public MoimDTO(String uid, long timestamp, String title, String subtitle, String image, String age_max, String age_min, /*UserDTO user,*/ String category){
        this.timestamp = timestamp;
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.uid = uid;
        this.age_max = age_max;
        this.age_min = age_min;
        this.category = category;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("uid",uid);
        dto.put("timestamp", timestamp);
        dto.put("title", title);
        dto.put("subtitle", subtitle);
        dto.put("image",image);
        dto.put("age_max", age_max);
        dto.put("age_min", age_min);
        dto.put("category", category);
        return dto;
    }
}