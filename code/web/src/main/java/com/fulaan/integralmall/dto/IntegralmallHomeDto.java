package com.fulaan.integralmall.dto;

import java.util.List;

/**
 * 
 * <简述>积分商城首页dto
 * <详细描述>
 * @author   yaojintao
 * @version  $Id$
 * @since
 * @see
 */
public class IntegralmallHomeDto {

    //我的积分
    private int score;
    
    //商品列表
    private List<GoodsDto> goodsList;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<GoodsDto> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsDto> goodsList) {
        this.goodsList = goodsList;
    }
    
    
    
}
