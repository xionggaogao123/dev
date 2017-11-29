package com.fulaan.backstage.dto;

import com.pojo.backstage.UserRoleOfPathEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/11/29.
 */
public class UserRoleOfPathDTO {

    private String id;
    private List<String> paths=new ArrayList<String>();
    private int role;

    public UserRoleOfPathDTO(){

    }

    public UserRoleOfPathDTO(UserRoleOfPathEntry pathEntry){
        this.id=pathEntry.getID().toString();
        this.paths.addAll(pathEntry.getPaths());
        this.role=pathEntry.getRole();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
