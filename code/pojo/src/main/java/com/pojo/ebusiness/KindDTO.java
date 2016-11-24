package com.pojo.ebusiness;

import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/1/14.
 */
public class KindDTO {
    private String kindId;
    private String kindName;
    private List<SpecDTO> specList = new ArrayList<SpecDTO>();

    public KindDTO(){}

    public KindDTO(EGoodsEntry.Kind kind){
        this.kindId = kind.getId().toString();
        this.kindName = kind.getName();
        List<EGoodsEntry.Spec> specs = kind.getList();
        if(null != specs){
            for(EGoodsEntry.Spec spec : specs){
                this.specList.add(new SpecDTO(spec));
            }
        }
    }

    public EGoodsEntry.Kind exportEntry(){
        List<EGoodsEntry.Spec> specs = new ArrayList<EGoodsEntry.Spec>();
        if(null != specList){
            for(SpecDTO spec : specList){
                specs.add(new EGoodsEntry.Spec(new ObjectId(spec.getId()), spec.getName(), spec.getPrice()));
            }
        }
        if(kindId.equals("")){
            kindId = new ObjectId().toString();
        }
        EGoodsEntry.Kind kind = new EGoodsEntry.Kind(new ObjectId(kindId), kindName, specs);
        return kind;
    }

    public String getKindId() {
        return kindId;
    }

    public void setKindId(String kindId) {
        this.kindId = kindId;
    }

    public String getKindName() {
        return kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public List<SpecDTO> getSpecList() {
        return specList;
    }

    public void setSpecList(List<SpecDTO> specList) {
        this.specList = specList;
    }
}
