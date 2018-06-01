package com.fulaan.business.dto;

/**
 * Created by James on 2018-06-01.
 */
public class VersionOpenDTO {

    private String id;
    private String moduleName;
    private int moduleType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getModuleType() {
        return moduleType;
    }

    public void setModuleType(int moduleType) {
        this.moduleType = moduleType;
    }
}
