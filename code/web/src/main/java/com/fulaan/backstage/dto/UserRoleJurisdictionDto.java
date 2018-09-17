package com.fulaan.backstage.dto;



import com.db.backstage.JurisdictionTreeDao;
import com.fulaan.txpay.Utils.StringUtil;
import com.pojo.backstage.JurisdictionTreeEntry;
import com.pojo.backstage.UserRoleJurisdictionEntry;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taotao.chan on 2018年8月22日16:52:32
 */
public class UserRoleJurisdictionDto {

    private String id;
    private String level;
    private List<String> roleJurisdiction;
    private List<String> rolePath;
    //用来展示权限tree value 对应的中文name
    private String roleJurisdictionName;
    private String createBy;
    private String creationDate;
    private String lastUpdateBy;
    private String lastUpdateDate;

    JurisdictionTreeDao jurisdictionTreeDao = new JurisdictionTreeDao();

    public UserRoleJurisdictionDto(UserRoleJurisdictionEntry entry){
        this.id = entry.getID().toString();
        this.level = entry.getLevel();
        this.roleJurisdiction = entry.getRoleJurisdiction();
        if (roleJurisdiction.size() != 0 && !"".equals(roleJurisdiction.get(0))){
            List<JurisdictionTreeEntry> jurisdictionTreeEntries = new ArrayList<JurisdictionTreeEntry>();
            List<String> classList = new ArrayList<String>();
            for (String roleJurisdictionId: roleJurisdiction){
                JurisdictionTreeEntry jurisdictionTreeEntry = jurisdictionTreeDao.getEntryById(new ObjectId(roleJurisdictionId));
                //权限分类去重获取
                if (!classList.contains(jurisdictionTreeEntry.getClas())){
                    classList.add(jurisdictionTreeEntry.getClas());
                }
                jurisdictionTreeEntries.add(jurisdictionTreeEntry);
            }
            String roleJurisdictionNameTemp = "";
            for (String clas : classList) {
                String clasEach = "";
                for (JurisdictionTreeEntry treeEntry : jurisdictionTreeEntries){
                    if (clas.equals(treeEntry.getClas())){
                        clasEach += treeEntry.getName()+" ";
                    }
                }
                clasEach = clas+"："+clasEach;
                roleJurisdictionNameTemp += clasEach;
            }
            this.roleJurisdictionName = roleJurisdictionNameTemp;
        }
        this.rolePath = entry.getRolePath();
        this.createBy = entry.getCreateBy().toString();
        this.creationDate = entry.getCreationDate();
        this.lastUpdateBy = entry.getLastUpdateBy().toString();
        this.lastUpdateDate = entry.getLastUpdateDate();
    }

//    public UserRoleJurisdictionDto(UserRoleOfPathEntry pathEntry){
//        this.id=pathEntry.getID().toString();
//        this.paths.addAll(pathEntry.getPaths());
//        this.role=pathEntry.getRole();
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getRoleJurisdiction() {
        return roleJurisdiction;
    }

    public void setRoleJurisdiction(List<String> roleJurisdiction) {
        this.roleJurisdiction = roleJurisdiction;
    }

    public List<String> getRolePath() {
        return rolePath;
    }

    public void setRolePath(List<String> rolePath) {
        this.rolePath = rolePath;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getRoleJurisdictionName() {
        return roleJurisdictionName;
    }

    public void setRoleJurisdictionName(String roleJurisdictionName) {
        this.roleJurisdictionName = roleJurisdictionName;
    }
}
