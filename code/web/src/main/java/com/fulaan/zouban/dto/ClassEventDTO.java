package com.fulaan.zouban.dto;

import com.fulaan.examresult.controller.IdNameDTO;
import com.pojo.zouban.IdNamePair;
import com.pojo.zouban.PointEntry;
import com.pojo.zouban.TimetableConfEntry;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/25.
 */
public class ClassEventDTO {
    private String id;
    private String name;
    private int x;
    private int y;
    private List<IdNameDTO> classList = new ArrayList<IdNameDTO>();


    public ClassEventDTO(){}

    public ClassEventDTO(TimetableConfEntry.ClassEvent event) {
        this.id = event.getID().toString();
        this.name = event.getName();
        this.x = event.getX();
        this.y = event.getY();
        for (IdNamePair inp : event.getClassList()) {
            this.classList.add(new IdNameDTO(inp.getId().toString(), inp.getName()));
        }
    }

    public TimetableConfEntry.ClassEvent exportEntry() {
        TimetableConfEntry.ClassEvent classEvent = new TimetableConfEntry.ClassEvent();
        if (!StringUtils.isEmpty(id)) {
            classEvent.setID(new ObjectId(id));
        } else {
            classEvent.setID(new ObjectId());
        }
        classEvent.setX(x);
        classEvent.setY(y);
        classEvent.setName(name);

        List<IdNamePair> idNamePairList = new ArrayList<IdNamePair>();
        for (IdNameDTO idNameDTO : classList) {
            idNamePairList.add(new IdNamePair(new ObjectId(idNameDTO.getId()), idNameDTO.getName()));
        }
        classEvent.setClassList(idNamePairList);

        return classEvent;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public List<IdNameDTO> getClassList() {
        return classList;
    }

    public void setClassList(List<IdNameDTO> classList) {
        this.classList = classList;
    }
}
