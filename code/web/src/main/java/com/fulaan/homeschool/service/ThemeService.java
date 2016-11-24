package com.fulaan.homeschool.service;

import com.db.microblog.ThemeDao;
import com.fulaan.homeschool.dto.ThemeDTO;
import com.pojo.microblog.ThemeEntry;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/12/14.
 */

@Service
public class ThemeService {

    private static final Logger logger= Logger.getLogger(ThemeService.class);

    private ThemeDao themeDao = new ThemeDao();

    @Autowired
    private HomeSchoolService homeSchoolService;

    /**
     * 添加话题
     * @param themeEntry
     * @return
     */
    public ObjectId addTheme(ThemeEntry themeEntry) {
        return themeDao.addTheme(themeEntry);
    }

    /**
     * 删除话题
     * @param id
     */
    public void deleteTheme(ObjectId id) {
        themeDao.deleteTheme(id);
    }

    /**
     * 跟新话题
     * @param id
     * @param themedsc
     */
    public void updateTheme(ObjectId id,String themedsc) {
        themeDao.updateTheme(id,themedsc);
    }

    /**
     * 获取话题
     * @return
     */
    public List<ThemeDTO> getThemeList(String schoolid) {
        List<ThemeDTO> themeDTOList = new ArrayList<ThemeDTO>();
        List<ThemeEntry> themeEntryList = themeDao.getThemeList();
        ThemeDTO themeDTO = null;
        if (themeEntryList!=null &&themeEntryList.size()!=0) {
            for (ThemeEntry theme : themeEntryList) {
                themeDTO = new ThemeDTO();
                themeDTO.setId(theme.getID().toString());
                themeDTO.setThemedsc(theme.getThemedec());
                themeDTO.setDelflg(theme.getDeflg());
                themeDTO.setCount(homeSchoolService.getThemeCount(theme.getThemedec(),schoolid));
                themeDTOList.add(themeDTO);
            }
        }
        return themeDTOList;
    }
    /**
     * 获取话题
     * @return
     */
    public List<ThemeDTO> getThemeList2() {
        List<ThemeDTO> themeDTOList = new ArrayList<ThemeDTO>();
        List<ThemeEntry> themeEntryList = themeDao.getThemeList2();
        ThemeDTO themeDTO = null;
        if (themeEntryList!=null &&themeEntryList.size()!=0) {
            for (ThemeEntry theme : themeEntryList) {
                themeDTO = new ThemeDTO();
                themeDTO.setId(theme.getID().toString());
                themeDTO.setThemedsc(theme.getThemedec());
                themeDTO.setDelflg(theme.getDeflg());
                themeDTOList.add(themeDTO);
            }
        }
        return themeDTOList;
    }
}
