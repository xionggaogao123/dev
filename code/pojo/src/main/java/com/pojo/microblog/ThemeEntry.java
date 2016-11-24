package com.pojo.microblog;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 ** 微博
 * <pre>
 * collectionName:theme
 * </pre>
 * <pre>
 * {
     themedec:话题
     deflg:是否删除
 * }
 * </pre>
 * Created by wang_xinxin on 2015/12/14.
 */
public class ThemeEntry extends BaseDBObject {

     private static final long serialVersionUID = 4992189615539775350L;

     public ThemeEntry(BasicDBObject baseEntry) {
          super(baseEntry);
     }

     public ThemeEntry(String themedec) {
          super();
          BasicDBObject baseEntry =new BasicDBObject()
                  .append("themedec", themedec)
                  .append("deflg",0);
          setBaseEntry(baseEntry);
     }

     public String getThemedec() {
          return getSimpleStringValue("themedec");
     }
     public void setThemedec(String themedec) {
          setSimpleValue("themedec", themedec);
     }
     public int getDeflg() {
          return getSimpleIntegerValue("deflg");
     }
     public void setDeflg(int deflg) {
          setSimpleValue("deflg",deflg);
     }
}
