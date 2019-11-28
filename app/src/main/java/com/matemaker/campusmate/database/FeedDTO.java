package com.matemaker.campusmate.database;

import java.util.HashMap;
import java.util.Map;

public class FeedDTO {
    String uid = null;
    Long number = null;

    FeedDTO(String uid, Long number){
        this.uid = uid;
        this.number = number;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("uid",uid);
        dto.put("number",number);
        return dto;
    }
}