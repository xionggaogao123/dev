package com.pojo.ebusiness;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by wangkaidong on 2016/3/9.
 */
public class EExpressTemplateDTO {
    private String id;
    private String name;
    private List<EExpressTemplateEntry.ExpTempDetail> details;

    public EExpressTemplateDTO(){}

    public EExpressTemplateDTO(EExpressTemplateEntry entry){
        this.id = entry.getID().toString();
        this.name = entry.getName();
        this.details = entry.getDetails();
    }

    public EExpressTemplateEntry exportEntry(){
        EExpressTemplateEntry entry = new EExpressTemplateEntry();
        if(id != null && !id.equals("")){
            entry.setID(new ObjectId(id));
        }
        entry.setName(name);
        entry.setDetails(details);

        return entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<EExpressTemplateEntry.ExpTempDetail> getDetails(){
        return details;
    }

    public void setDetails(List<EExpressTemplateEntry.ExpTempDetail> details){
        this.details = details;
    }
}
