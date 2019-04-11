package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

public class AccountChangeQueryDto {

    private String m6AvgCreditOut;
    private String m6AvgDebitIn;
    private String cardIndex;
    private String blackListStr;
    private String queryMonth;
    private String areaProvince;
    private String areaCity;
    
    
    public List<FieldCondition> generateFieldCondition(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
        
        if(StringUtils.isNotBlank(m6AvgCreditOut)){
            fieldConditions.add(
                new FieldCondition(Constants.ACM_AVG_CREDIT_OUT, m6AvgCreditOut, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m6AvgDebitIn)){
            fieldConditions.add(
                new FieldCondition(Constants.ACM_AVG_DEBIT_IN, m6AvgDebitIn, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(queryMonth)){
        	fieldConditions.add(
                    new FieldCondition(Constants.ACM_QUERY_MONTH, queryMonth, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(cardIndex)){
            fieldConditions.add(
                new FieldCondition(Constants.ACM_CARD_INDEX, cardIndex, ConditionType.BETWEEN));
        }
        
        // 特殊名单过滤
        if(StringUtils.isNotBlank(blackListStr)){
            for(String blackList : blackListStr.split(",")){
                FieldCondition fieldCondition = new FieldCondition(
                    Constants.BLACK_LIST_TAG.replace("{tag}", blackList), "0", ConditionType.EQUAL);
                fieldConditions.add(fieldCondition);
            }
        }
        // location 
        if(StringUtils.isNotBlank(areaProvince)){
            FieldCondition fieldCondition = new FieldCondition(Constants.AREA_PROVINCE, areaProvince, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(areaCity)){
            FieldCondition fieldCondition = new FieldCondition(Constants.AREA_CITY, areaCity, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        return fieldConditions;
    }
    /**
     * @return the {@link #m6AvgCreditOut}
     */
    public String getM6AvgCreditOut() {
        return m6AvgCreditOut;
    }
    /**
     * @param m6AvgCreditOut
     * the {@link #m6AvgCreditOut} to set
     */
    public void setM6AvgCreditOut(String m6AvgCreditOut) {
        this.m6AvgCreditOut = m6AvgCreditOut;
    }
    /**
     * @return the {@link #m6AvgDebitIn}
     */
    public String getM6AvgDebitIn() {
        return m6AvgDebitIn;
    }
    /**
     * @param m6AvgDebitIn
     * the {@link #m6AvgDebitIn} to set
     */
    public void setM6AvgDebitIn(String m6AvgDebitIn) {
        this.m6AvgDebitIn = m6AvgDebitIn;
    }
    /**
     * @return the {@link #cardIndex}
     */
    public String getCardIndex() {
        return cardIndex;
    }
    /**
     * @param cardIndex
     * the {@link #cardIndex} to set
     */
    public void setCardIndex(String cardIndex) {
        this.cardIndex = cardIndex;
    }
    /**
     * @return the {@link #blackListStr}
     */
    public String getBlackListStr() {
        return blackListStr;
    }
    /**
     * @param blackListStr
     * the {@link #blackListStr} to set
     */
    public void setBlackListStr(String blackListStr) {
        this.blackListStr = blackListStr;
    }
	/**
	 * @return the areaProvince
	 */
	public String getAreaProvince() {
		return areaProvince;
	}
	/**
	 * @param areaProvince the areaProvince to set
	 */
	public void setAreaProvince(String areaProvince) {
		this.areaProvince = areaProvince;
	}
	/**
	 * @return the areaCity
	 */
	public String getAreaCity() {
		return areaCity;
	}
	/**
	 * @param areaCity the areaCity to set
	 */
	public void setAreaCity(String areaCity) {
		this.areaCity = areaCity;
	}
	/**
	 * @return the queryMonth
	 */
	public String getQueryMonth() {
		return queryMonth;
	}
	/**
	 * @param queryMonth the queryMonth to set
	 */
	public void setQueryMonth(String queryMonth) {
		this.queryMonth = queryMonth;
	}
    
}
