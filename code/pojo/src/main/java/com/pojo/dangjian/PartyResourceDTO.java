package com.pojo.dangjian;

import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO1;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/3/23.
 */
public class PartyResourceDTO {
    private String id;
    private String name;
    private List<IdNameValuePairDTO> srcs = new ArrayList<IdNameValuePairDTO>();
    private String directory;
    private String term;
    private int state;
    private String userId;

    private int isMine;
    private int dirType;

    public PartyResourceDTO(){}

    public PartyResourceDTO(PartyResourceEntry entry){
        id = entry.getID().toString();
        name = entry.getName();
        List<IdNameValuePair> idNameValuePairs = entry.getSrcs();
        for(IdNameValuePair idNameValuePair : idNameValuePairs){
            srcs.add(new IdNameValuePairDTO(idNameValuePair));
        }
        directory = entry.getDirectoryId().toString();
        term = entry.getTerm();
        state = entry.getState();
        userId = entry.getUserId().toString();
    }

    public PartyResourceEntry exportEntry(){
        List<IdNameValuePair> pairs = new ArrayList<IdNameValuePair>();
        for(IdNameValuePairDTO src : srcs){
            pairs.add(src.exportEntry());
        }
        PartyResourceEntry entry = new PartyResourceEntry(name, pairs, new ObjectId(directory), term, new ObjectId(userId), state);
        if(!id.equals("")){
            entry.setID(new ObjectId(id));
        }
        return entry;
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

    public List<IdNameValuePairDTO> getSrcs() {
        return srcs;
    }

    public void setSrcs(List<IdNameValuePairDTO> srcs) {
        this.srcs = srcs;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public int getDirType() {
        return dirType;
    }

    public void setDirType(int dirType) {
        this.dirType = dirType;
    }
}
