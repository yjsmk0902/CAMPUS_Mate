package com.matemaker.campusmate.database;


import java.util.HashMap;
import java.util.Map;

public class BoardDTO {
    public String uid = null;
    public String number = null;
    public Long timestamp = null;
    public String title = null;
    public String contents = null;

    public BoardDTO(){}

    public BoardDTO(String uid, String number, Long timestamp, String title, String contents){
        this.uid = uid;
        this.number = number;
        this.timestamp = timestamp;
        this.title = title;
        this.contents = contents;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("uid",uid);
        dto.put("number",number);
        dto.put("timestamp",timestamp);
        dto.put("title",title);
        dto.put("contents",contents);
        return dto;
    }
}