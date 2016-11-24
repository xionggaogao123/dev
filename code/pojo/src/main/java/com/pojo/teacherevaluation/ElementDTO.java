package com.pojo.teacherevaluation;

import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO1;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/21.
 */
public class ElementDTO {

    private String id;
    private String schoolId;
    private String evaluationId;
    private String year;
    private String name;
    private double score;
    private int type;
    private List<IdValuePairDTO1> contents = new ArrayList<IdValuePairDTO1>();

    public ElementDTO(){}

    public ElementDTO(ElementEntry elementEntry){
        id = elementEntry.getID().toString();
        schoolId = elementEntry.getSchoolId().toString();
        evaluationId = elementEntry.getEvaluationId().toString();
        year = elementEntry.getYear();
        name = elementEntry.getName();
        score = elementEntry.getElementScore();
        type = elementEntry.getType();
        List<IdValuePair> pairs = elementEntry.getElementContent();
        if(pairs.size() > 0){
            for(IdValuePair pair : pairs){
                contents.add(new IdValuePairDTO1(pair));
            }
        }
    }

    public ElementEntry exportEntry(){
        List<IdValuePair> pairs = new ArrayList<IdValuePair>();
        if(contents.size() > 0){
            for(IdValuePairDTO1 content : contents){
                pairs.add(content.exportEntry());
            }
        }
        ElementEntry elementEntry = new ElementEntry(new ObjectId(schoolId), year, new ObjectId(evaluationId), name, score, type, pairs);
        if(null != id){
            elementEntry.setID(new ObjectId(id));
        }
        return elementEntry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<IdValuePairDTO1> getContents() {
        return contents;
    }

    public void setContents(List<IdValuePairDTO1> contents) {
        this.contents = contents;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }
}
