package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

/**
 * 媒体查询dto
 * @author chun
 *
 */
public class MediaQueryDto {

    private String cateName;
    private String visitday;
    private String month;
    
    public MediaQueryDto(){
    }

    /**
     * 生成media查询过滤条件
     * @return
     */
    public List<FieldCondition> generateFieldConditions(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
            
        if(StringUtils.isNotBlank(cateName)){
            FieldCondition fieldCondition = null;
            
            if(StringUtils.isNotBlank(visitday)){
                fieldCondition = new FieldCondition(
                    Constants.MEDIA_CATE_VISITDAY.replace("{cate}", cateName), visitday, ConditionType.BETWEEN);
                fieldConditions.add(fieldCondition);
            }
            if(StringUtils.isNotBlank(month)){
                fieldCondition = new FieldCondition(
                    Constants.MEDIA_CATE_LAST_MONTH.replace("{cate}", cateName), month, ConditionType.BETWEEN);
                fieldConditions.add(fieldCondition);
            }
        }
        return fieldConditions;
        
    }
    /**
     * @return the {@link #cateName}
     */
    public String getCateName() {
        return cateName;
    }
    /**
     * @param cateName
     * the {@link #cateName} to set
     */
    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
    /**
     * @return the {@link #visitday}
     */
    public String getVisitday() {
        return visitday;
    }
    /**
     * @param visitday
     * the {@link #visitday} to set
     */
    public void setVisitday(String visitday) {
        this.visitday = visitday;
    }
    
}
