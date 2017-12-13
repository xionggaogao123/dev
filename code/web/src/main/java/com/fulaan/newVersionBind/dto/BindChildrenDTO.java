package com.fulaan.newVersionBind.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/12/13.
 */
public class BindChildrenDTO {

    private List<String> children = new ArrayList<String>();

    public BindChildrenDTO(){

    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
