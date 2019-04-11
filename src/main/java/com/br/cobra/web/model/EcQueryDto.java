package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

/**
 * ec查询dto
 * @author chun
 *
 */
public class EcQueryDto {

    private String cateName;
    private String visit;
    private String buy;
    private String pay;
    private String month;
    
    public EcQueryDto(){
        
    }
    
    /**
     * 生成查询参数条件
     * @return
     */
    public List<FieldCondition> generateFieldConditions(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
        if(StringUtils.isNotBlank(cateName)){
            FieldCondition fieldCondition = null;
            
            if(StringUtils.isNotBlank(visit)){
                fieldCondition = new FieldCondition(
                    Constants.EC_CATE_VISIT.replace("{cate}", cateName), visit, ConditionType.BETWEEN);
                fieldConditions.add(fieldCondition);
            }
            
            if(StringUtils.isNotBlank(buy)){
                fieldCondition = new FieldCondition(
                    Constants.EC_CATE_NUM.replace("{cate}", cateName), buy, ConditionType.BETWEEN);
                fieldConditions.add(fieldCondition);
            }
            
            if(StringUtils.isNotBlank(pay)){
                fieldCondition = new FieldCondition(
                    Constants.EC_CATE_PAY.replace("{cate}", cateName), pay, ConditionType.BETWEEN);
                fieldConditions.add(fieldCondition);
            }
            if(StringUtils.isNotBlank(month)){
                fieldCondition = new FieldCondition(
                    Constants.EC_CATE_LAST_MONTH.replace("{cate}", cateName), month, ConditionType.BETWEEN);
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
     * @return the {@link #visit}
     */
    public String getVisit() {
        return visit;
    }
    /**
     * @param visit
     * the {@link #visit} to set
     */
    public void setVisit(String visit) {
        this.visit = visit;
    }
    /**
     * @return the {@link #buy}
     */
    public String getBuy() {
        return buy;
    }
    /**
     * @param buy
     * the {@link #buy} to set
     */
    public void setBuy(String buy) {
        this.buy = buy;
    }
    /**
     * @return the {@link #pay}
     */
    public String getPay() {
        return pay;
    }
    /**
     * @param pay
     * the {@link #pay} to set
     */
    public void setPay(String pay) {
        this.pay = pay;
    }

    /**
     * @return the {@link #month}
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month
     * the {@link #month} to set
     */
    public void setMonth(String month) {
        this.month = month;
    }
    
}
