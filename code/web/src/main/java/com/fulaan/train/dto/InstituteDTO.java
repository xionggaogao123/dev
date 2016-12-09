package com.fulaan.train.dto;

import com.pojo.questions.PropertiesObj;
import com.pojo.train.InstituteEntry;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/12/2.
 */
public class InstituteDTO {

    private String id;
    private String name;
    private String mainPicture;
    private String address;
    private String telephone;
    private String createTime;
    private String description;
    private String businessTime;
    private String server;
    private String score;
    private List<String> typeNames=new ArrayList<String>();
    private List<String> regionNames=new ArrayList<String>();
    private List<Integer> scoreList=new ArrayList<Integer>();
    private List<Integer> unScoreList=new ArrayList<Integer>();
    private double lon;
    private double lat;
    private String distance;
    private String imageUrl;

    public InstituteDTO(){

    }

    public InstituteDTO(InstituteEntry entry){
        this.id=entry.getID().toString();
        this.name=entry.getName();
        this.mainPicture=entry.getMainPic();
        this.address=entry.getAddress();
        this.telephone=entry.getTelephone();
        this.createTime=entry.getChuangLiShiJian();
        this.description=entry.getShangHuJianJie();
        this.businessTime=entry.getYingYeShiJian();
        this.server=entry.getTeSeFuWu();
        this.score=String.format("%.1f",entry.getScore());
        this.imageUrl=dealImage(entry.getImageUrl(),entry.getMainPic());
        List<PropertiesObj> types=entry.getTypes();
        for(PropertiesObj obj:types){
            typeNames.add(obj.getName());
        }
        List<PropertiesObj> regions=entry.getAreas();
        for(PropertiesObj obj:regions){
            regionNames.add(obj.getName());
        }
        if(null!=entry.getLocations()){
            getLocation(entry.getLocations());
        }

    }

    public String dealImage(String imageUrl,String mainPicture){
        if(StringUtils.isNotBlank(imageUrl)){
            return  imageUrl;
        }else{
            return mainPicture;
        }
    }

    public void getLocation(InstituteEntry.Locations locations){
        List<Double> accordinates=locations.getCoordinates();
        this.lon=accordinates.get(0);
        this.lat=accordinates.get(1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(List<String> typeNames) {
        this.typeNames = typeNames;
    }

    public List<String> getRegionNames() {
        return regionNames;
    }

    public void setRegionNames(List<String> regionNames) {
        this.regionNames = regionNames;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public List<Integer> getUnScoreList() {
        return unScoreList;
    }

    public void setUnScoreList(List<Integer> unScoreList) {
        this.unScoreList = unScoreList;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
