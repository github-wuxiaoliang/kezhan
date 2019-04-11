package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

public class SocietyRelationQueryDto {

    /**
     * 社交帐号匹配类型 1：通过cell注册微博 ；2：通过mail注册微博；3：通过cell及mail注册微博
     */
    private String matchType;
    /**
     * 用户类型标识 1 普通用户2 认证用户4 未知7 微博达人10 微博女郎
     */
    private String userType;
    /**
     * M：男 ，F：女
     */
    private String gender;
    private String tagStr;
    /**
     * 等级
     */
    private String level;
    private String followNum;
    private String fansNum;
    private String spread;
    private String industryStr;
    private String location;
    private String weiboNum;
    private String blackListStr;
    
    public List<FieldCondition> generateFieldCondition(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
        
        if(StringUtils.isNotBlank(matchType)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_MATCH_TYPE, matchType, ConditionType.EQUAL));
        }
        if(StringUtils.isNotBlank(userType)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_USER_TYPE, userType, ConditionType.EQUAL));
        }
        if(StringUtils.isNotBlank(gender)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_GENDER, gender, ConditionType.EQUAL));
        }
        if(StringUtils.isNotBlank(tagStr)){
            for(String tag : tagStr.split(",")){
                fieldConditions.add(
                    new FieldCondition(Constants.SR_INT_TAG.replace("{tag}", tag), "1", ConditionType.EQUAL));
            }
        }
        if(StringUtils.isNotBlank(level)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_LEVEL, level, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(followNum)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_FOLLOW_NUM, followNum, ConditionType.BETWEEN));
        }
        if(StringUtils.isNotBlank(fansNum)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_FANS_NUM, fansNum, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(spread)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_SPREAD, spread, ConditionType.BETWEEN));
        }
        
        if(StringUtils.isNotBlank(industryStr)){
            for(String industry : industryStr.split(",")){
                fieldConditions.add(
                    new FieldCondition(Constants.SR_INDUSTRY.replace("{industry}", industry), "1", ConditionType.EQUAL));
            }
        }
        
        if(StringUtils.isNotBlank(location)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_LOCATION, location, ConditionType.EQUAL));
        }
        
        if(StringUtils.isNotBlank(weiboNum)){
            fieldConditions.add(
                new FieldCondition(Constants.SR_WEIBO_NUM, weiboNum, ConditionType.BETWEEN));
        }
        
        // 特殊名单过滤
        if(StringUtils.isNotBlank(blackListStr)){
            for(String blackList : blackListStr.split(",")){
                FieldCondition fieldCondition = new FieldCondition(
                    Constants.BLACK_LIST_TAG.replace("{tag}", blackList), "0", ConditionType.EQUAL);
                fieldConditions.add(fieldCondition);
            }
        }
        
        return fieldConditions;
    }
    
    
    /**
     * @return the {@link #matchType}
     */
    public String getMatchType() {
        return matchType;
    }
    /**
     * @param matchType
     * the {@link #matchType} to set
     */
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
    /**
     * @return the {@link #userType}
     */
    public String getUserType() {
        return userType;
    }
    /**
     * @param userType
     * the {@link #userType} to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
    /**
     * @return the {@link #gender}
     */
    public String getGender() {
        return gender;
    }
    /**
     * @param gender
     * the {@link #gender} to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    /**
     * @return the {@link #tagStr}
     */
    public String getTagStr() {
        return tagStr;
    }
    /**
     * @param tagStr
     * the {@link #tagStr} to set
     */
    public void setTagStr(String tagStr) {
        this.tagStr = tagStr;
    }
    /**
     * @return the {@link #level}
     */
    public String getLevel() {
        return level;
    }
    /**
     * @param level
     * the {@link #level} to set
     */
    public void setLevel(String level) {
        this.level = level;
    }
    /**
     * @return the {@link #followNum}
     */
    public String getFollowNum() {
        return followNum;
    }
    /**
     * @param followNum
     * the {@link #followNum} to set
     */
    public void setFollowNum(String followNum) {
        this.followNum = followNum;
    }
    /**
     * @return the {@link #fansNum}
     */
    public String getFansNum() {
        return fansNum;
    }
    /**
     * @param fansNum
     * the {@link #fansNum} to set
     */
    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }
    /**
     * @return the {@link #spread}
     */
    public String getSpread() {
        return spread;
    }
    /**
     * @param spread
     * the {@link #spread} to set
     */
    public void setSpread(String spread) {
        this.spread = spread;
    }
    /**
     * @return the {@link #industryStr}
     */
    public String getIndustryStr() {
        return industryStr;
    }
    /**
     * @param industryStr
     * the {@link #industryStr} to set
     */
    public void setIndustryStr(String industryStr) {
        this.industryStr = industryStr;
    }
    /**
     * @return the {@link #location}
     */
    public String getLocation() {
        return location;
    }
    /**
     * @param location
     * the {@link #location} to set
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * @return the {@link #weiboNum}
     */
    public String getWeiboNum() {
        return weiboNum;
    }
    /**
     * @param weiboNum
     * the {@link #weiboNum} to set
     */
    public void setWeiboNum(String weiboNum) {
        this.weiboNum = weiboNum;
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
    
}
