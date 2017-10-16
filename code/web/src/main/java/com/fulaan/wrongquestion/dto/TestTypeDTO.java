package com.fulaan.wrongquestion.dto;

import com.pojo.wrongquestion.TestTypeEntry;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/10/12.
 */
public class TestTypeDTO {
    private String id;
    private String name;
    private String sename;



    public TestTypeDTO(){

    }
    public TestTypeDTO(TestTypeEntry e){
        if(e!=null){
            this.id = e.getID()==null?"":e.getID().toString();
            this.name = e.getName();
            this.sename = e.getSename();
        }else{
            new TestTypeDTO();
        }
    }

    public TestTypeEntry buildAddEntry(){
        TestTypeEntry openEntry =
                new TestTypeEntry(
                        this.name,
                        this.sename
                );
        return openEntry;

    }

    public TestTypeEntry updateEntry(){
        ObjectId Id=null;
        if(this.getId()!=null&&!"".equals(this.getId())){
            Id=new ObjectId(this.getId());
        }
        TestTypeEntry openEntry =
                new TestTypeEntry(
                        Id,
                        this.name,
                        this.sename
                );
        return openEntry;

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

    public String getSename() {
        return sename;
    }

    public void setSename(String sename) {
        this.sename = sename;
    }
}
