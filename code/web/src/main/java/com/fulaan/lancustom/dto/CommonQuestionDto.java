package com.fulaan.lancustom.dto;

import org.bson.types.ObjectId;

import com.pojo.lancustom.CommonQuestionEntry;

import cn.jiguang.commom.utils.StringUtils;

/**
 * 
 * <简述> 常见问题
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class CommonQuestionDto {

    private String id;
    
    private String question;
    
    private String answer;
    
    private String pid;
    
    private int type;
    
    private String typee;

    public CommonQuestionDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CommonQuestionDto(CommonQuestionEntry c) {
        super();
        if ( c != null) {
            this.id = c.getID() == null ?"":c.getID().toString();
            this.question = c.getQuestion();
            this.answer = c.getAnswer();
            this.pid = c.getPid() == null?"":c.getPid().toString();
            this.type = c.getType();

//            if (c.getType() == 1) {
//                this.typee = "账号及登录问题";
//            } else if (c.getType() == 2) {
//                this.typee = "关联孩子问题";
//            } else if (c.getType() == 3) {
//                this.typee = "作业使用问题";
//            } else if (c.getType() == 4) {
//                this.typee = "成绩单使用问题";
//            } else if (c.getType() == 5) {
//                this.typee = "效管控使用问题";
//            } else if (c.getType() == 6) {
//                this.typee = "家管控使用问题";
//            } else {
//                this.typee = "其他问题";
//            }
            //新版本要编辑模块名称
            if (c.getClasses() != null){
                this.typee = c.getClasses();
            }else {
                if (c.getType() == 1) {
                    this.typee = "账号及登录问题";
                } else if (c.getType() == 2) {
                    this.typee = "关联孩子问题";
                } else if (c.getType() == 3) {
                    this.typee = "作业使用问题";
                } else if (c.getType() == 4) {
                    this.typee = "成绩单使用问题";
                } else if (c.getType() == 5) {
                    this.typee = "效管控使用问题";
                } else if (c.getType() == 6) {
                    this.typee = "家管控使用问题";
                } else {
                    this.typee = "其他问题";
                }
            }
        } else {
            new CommonQuestionDto();
        }
        
    }
    
    public CommonQuestionEntry buildAddEntry(){
        CommonQuestionEntry entry;
        if (StringUtils.isNotEmpty(this.pid)) {
            entry = new CommonQuestionEntry(this.question, this.answer, new ObjectId(this.pid),type);
        } else {
            entry = new CommonQuestionEntry(this.question, this.answer,type);
        }
        
        return entry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypee() {
        return typee;
    }

    public void setTypee(String typee) {
        this.typee = typee;
    }
    
    
    
    
}
