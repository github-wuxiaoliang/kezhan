package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

/**
 * 航旅行为查询参数
 * @author chun
 *
 */
public class AirTravelQueryDto {

    private String m24TotalNum;
    private String m24TotalFirstNum;
    private String m24TotalBusinessNum;
    private String m24TotalEconomyNum;
    private String m12TotalNum;
    private String m12TotalFirstNum;
    private String m12TotalBusinessNum;
    private String m12TotalEconomyNum;
    private String m6TotalNum;
    private String m6TotalFirstNum;
    private String m6TotalBusinessNum;
    private String m6TotalEconomyNum;
    private String queryMonth;
    
    private String areaProvince;
    private String areaCity;
    private String sex;
    private String birthYear;
    private String hasCell;
    private String blackListStr;
    
    
    public List<FieldCondition> generateFieldCondition(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
        
        if(StringUtils.isNotBlank(m24TotalNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_TOTAL_NUM.replace("{QUARTER}", "m24"), m24TotalNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m24TotalFirstNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_FIRST_NUM.replace("{QUARTER}", "m24"), m24TotalFirstNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m24TotalBusinessNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_BUSINESS_NUM.replace("{QUARTER}", "m24"), m24TotalBusinessNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m24TotalEconomyNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_ECONOMY_NUM.replace("{QUARTER}", "m24"), m24TotalEconomyNum, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(queryMonth)){
        	fieldConditions.add(
                    new FieldCondition(Constants.TRAVEL_MONTH, queryMonth, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(m12TotalNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_TOTAL_NUM.replace("{QUARTER}", "m12"), m12TotalNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m12TotalFirstNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_FIRST_NUM.replace("{QUARTER}", "m12"), m12TotalFirstNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m12TotalBusinessNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_BUSINESS_NUM.replace("{QUARTER}", "m12"), m12TotalBusinessNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m12TotalEconomyNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_ECONOMY_NUM.replace("{QUARTER}", "m12"), m12TotalEconomyNum, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(m6TotalNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_TOTAL_NUM.replace("{QUARTER}", "m6"), m6TotalNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m6TotalFirstNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_FIRST_NUM.replace("{QUARTER}", "m6"), m6TotalFirstNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m6TotalBusinessNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_BUSINESS_NUM.replace("{QUARTER}", "m6"), m6TotalBusinessNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(m6TotalEconomyNum)){
            fieldConditions.add(
                new FieldCondition(Constants.TRAVEL_QUARTER_ECONOMY_NUM.replace("{QUARTER}", "m6"), m6TotalEconomyNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(hasCell)){
            fieldConditions.add(
                new FieldCondition(Constants.RELATE_CELL_TYPE, hasCell, ConditionType.EQUAL));
        }
        // 特殊名单过滤
        if(StringUtils.isNotBlank(blackListStr)){
            for(String blackList : blackListStr.split(",")){
                FieldCondition fieldCondition = new FieldCondition(
                    Constants.BLACK_LIST_TAG.replace("{tag}", blackList), "0", ConditionType.EQUAL);
                fieldConditions.add(fieldCondition);
            }
        }
        
        if(StringUtils.isNotBlank(sex)){
            FieldCondition fieldCondition = new FieldCondition(Constants.SEX, sex, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        if(StringUtils.isNotBlank(birthYear)){
            FieldCondition fieldCondition = new FieldCondition(Constants.BIRTH_YEAR, birthYear, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
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
     * @return the {@link #m24TotalNum}
     */
    public String getM24TotalNum() {
        return m24TotalNum;
    }


    /**
     * @param m24TotalNum
     * the {@link #m24TotalNum} to set
     */
    public void setM24TotalNum(String m24TotalNum) {
        this.m24TotalNum = m24TotalNum;
    }


    /**
     * @return the {@link #m24TotalFirstNum}
     */
    public String getM24TotalFirstNum() {
        return m24TotalFirstNum;
    }


    /**
     * @param m24TotalFirstNum
     * the {@link #m24TotalFirstNum} to set
     */
    public void setM24TotalFirstNum(String m24TotalFirstNum) {
        this.m24TotalFirstNum = m24TotalFirstNum;
    }


    /**
     * @return the {@link #m24TotalBusinessNum}
     */
    public String getM24TotalBusinessNum() {
        return m24TotalBusinessNum;
    }


    /**
     * @param m24TotalBusinessNum
     * the {@link #m24TotalBusinessNum} to set
     */
    public void setM24TotalBusinessNum(String m24TotalBusinessNum) {
        this.m24TotalBusinessNum = m24TotalBusinessNum;
    }


    /**
     * @return the {@link #m24TotalEconomyNum}
     */
    public String getM24TotalEconomyNum() {
        return m24TotalEconomyNum;
    }


    /**
     * @param m24TotalEconomyNum
     * the {@link #m24TotalEconomyNum} to set
     */
    public void setM24TotalEconomyNum(String m24TotalEconomyNum) {
        this.m24TotalEconomyNum = m24TotalEconomyNum;
    }


    /**
     * @return the {@link #m12TotalNum}
     */
    public String getM12TotalNum() {
        return m12TotalNum;
    }


    /**
     * @param m12TotalNum
     * the {@link #m12TotalNum} to set
     */
    public void setM12TotalNum(String m12TotalNum) {
        this.m12TotalNum = m12TotalNum;
    }


    /**
     * @return the {@link #m12TotalFirstNum}
     */
    public String getM12TotalFirstNum() {
        return m12TotalFirstNum;
    }


    /**
     * @param m12TotalFirstNum
     * the {@link #m12TotalFirstNum} to set
     */
    public void setM12TotalFirstNum(String m12TotalFirstNum) {
        this.m12TotalFirstNum = m12TotalFirstNum;
    }


    /**
     * @return the {@link #m12TotalBusinessNum}
     */
    public String getM12TotalBusinessNum() {
        return m12TotalBusinessNum;
    }


    /**
     * @param m12TotalBusinessNum
     * the {@link #m12TotalBusinessNum} to set
     */
    public void setM12TotalBusinessNum(String m12TotalBusinessNum) {
        this.m12TotalBusinessNum = m12TotalBusinessNum;
    }


    /**
     * @return the {@link #m12TotalEconomyNum}
     */
    public String getM12TotalEconomyNum() {
        return m12TotalEconomyNum;
    }


    /**
     * @param m12TotalEconomyNum
     * the {@link #m12TotalEconomyNum} to set
     */
    public void setM12TotalEconomyNum(String m12TotalEconomyNum) {
        this.m12TotalEconomyNum = m12TotalEconomyNum;
    }


    /**
     * @return the {@link #m6TotalNum}
     */
    public String getM6TotalNum() {
        return m6TotalNum;
    }


    /**
     * @param m6TotalNum
     * the {@link #m6TotalNum} to set
     */
    public void setM6TotalNum(String m6TotalNum) {
        this.m6TotalNum = m6TotalNum;
    }


    /**
     * @return the {@link #m6TotalFirstNum}
     */
    public String getM6TotalFirstNum() {
        return m6TotalFirstNum;
    }


    /**
     * @param m6TotalFirstNum
     * the {@link #m6TotalFirstNum} to set
     */
    public void setM6TotalFirstNum(String m6TotalFirstNum) {
        this.m6TotalFirstNum = m6TotalFirstNum;
    }


    /**
     * @return the {@link #m6TotalBusinessNum}
     */
    public String getM6TotalBusinessNum() {
        return m6TotalBusinessNum;
    }


    /**
     * @param m6TotalBusinessNum
     * the {@link #m6TotalBusinessNum} to set
     */
    public void setM6TotalBusinessNum(String m6TotalBusinessNum) {
        this.m6TotalBusinessNum = m6TotalBusinessNum;
    }


    /**
     * @return the {@link #m6TotalEconomyNum}
     */
    public String getM6TotalEconomyNum() {
        return m6TotalEconomyNum;
    }


    /**
     * @param m6TotalEconomyNum
     * the {@link #m6TotalEconomyNum} to set
     */
    public void setM6TotalEconomyNum(String m6TotalEconomyNum) {
        this.m6TotalEconomyNum = m6TotalEconomyNum;
    }


    /**
     * @return the {@link #areaProvince}
     */
    public String getAreaProvince() {
        return areaProvince;
    }


    /**
     * @param areaProvince
     * the {@link #areaProvince} to set
     */
    public void setAreaProvince(String areaProvince) {
        this.areaProvince = areaProvince;
    }


    /**
     * @return the {@link #areaCity}
     */
    public String getAreaCity() {
        return areaCity;
    }


    /**
     * @param areaCity
     * the {@link #areaCity} to set
     */
    public void setAreaCity(String areaCity) {
        this.areaCity = areaCity;
    }


    /**
     * @return the {@link #sex}
     */
    public String getSex() {
        return sex;
    }


    /**
     * @param sex
     * the {@link #sex} to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }


    /**
     * @return the {@link #birthYear}
     */
    public String getBirthYear() {
        return birthYear;
    }


    /**
     * @param birthYear
     * the {@link #birthYear} to set
     */
    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }


    /**
     * @return the {@link #hasCell}
     */
    public String getHasCell() {
        return hasCell;
    }


    /**
     * @param hasCell
     * the {@link #hasCell} to set
     */
    public void setHasCell(String hasCell) {
        this.hasCell = hasCell;
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
