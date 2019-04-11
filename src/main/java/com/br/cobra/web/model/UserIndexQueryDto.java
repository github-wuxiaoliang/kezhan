package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

/**
 * 群体画像查询条件
 * @author chun
 *
 */
public class UserIndexQueryDto {

    private List<EcQueryDto> ecQueryDtos;
    private List<MediaQueryDto> mediaQueryDtos;
    private String title;
    private String assetHouse;
    private String assetCar;
    private String assetFin;
    private String assetWealth;
    private String applyLoanCount;
    private String applyLoanOrgCount;
    private String areaProvince;
    private String areaCity;
    private String type;
    private String ecQueryJson;
    private String mediaQueryJson;
    private String brandListStr;
    private String blackListStr;
    private String sex;
    private String birthYear;
    
    public UserIndexQueryDto(){
        ecQueryDtos = new ArrayList<EcQueryDto>();
        mediaQueryDtos = new ArrayList<MediaQueryDto>();
    }
    
    public List<FieldCondition> generateFieldCondition(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
        if(ecQueryDtos != null && !ecQueryDtos.isEmpty()){
            for(EcQueryDto ecQueryDto : ecQueryDtos){
                fieldConditions.addAll(ecQueryDto.generateFieldConditions());
            }
        }
        if(mediaQueryDtos != null && !mediaQueryDtos.isEmpty()){
            for(MediaQueryDto mediaQueryDto : mediaQueryDtos){
                fieldConditions.addAll(mediaQueryDto.generateFieldConditions());
            }
        }
        
        FieldCondition fieldCondition = null;
        // 品牌查询过滤条件
        if(StringUtils.isNotBlank(brandListStr)){
            for(String brand : brandListStr.split(",")){
                fieldCondition = new FieldCondition(
                    Constants.BRAND.replace("{brand}", brand), "1", ConditionType.EQUAL);
                fieldConditions.add(fieldCondition);
            }
        }
        
        // title
        if(StringUtils.isNotBlank(title)){
            fieldCondition = new FieldCondition(
                Constants.TITLE, title, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        //asset_house
        if(StringUtils.isNotBlank(assetHouse)){
            fieldCondition = new FieldCondition(
                Constants.ASSET_HOUSE, assetHouse, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        // asset_car
        if(StringUtils.isNotBlank(assetCar)){
            fieldCondition = new FieldCondition(
                Constants.ASSET_CAR, assetCar, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        // asset_fin
        if(StringUtils.isNotBlank(assetFin)){
            fieldCondition = new FieldCondition(
                Constants.ASSET_FIN, assetFin, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        // asset_weath
        if(StringUtils.isNotBlank(assetWealth)){
            fieldCondition = new FieldCondition(
                Constants.ASSET_WEALTH, assetWealth, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        // location 
        if(StringUtils.isNotBlank(areaProvince)){
            fieldCondition = new FieldCondition(
                Constants.AREA_PROVINCE, areaProvince, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(areaCity)){
            fieldCondition = new FieldCondition(
                Constants.AREA_CITY, areaCity, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        // applyloan
        if(StringUtils.isNotBlank(applyLoanCount)){
            fieldCondition = new FieldCondition(
                Constants.APPLY_LOAN_COUNT, applyLoanCount, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(applyLoanOrgCount)){
            fieldCondition = new FieldCondition(
                Constants.APPLY_LOAN_ORG_COUNT, applyLoanOrgCount, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        
        // 设置key类型
        if(StringUtils.isNotBlank(type)){
            fieldCondition = new FieldCondition(Constants.KEY_TYPE, type, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        // 特殊名单过滤
        if(StringUtils.isNotBlank(blackListStr)){
            for(String blackList : blackListStr.split(",")){
                fieldCondition = new FieldCondition(
                    Constants.BLACK_LIST_TAG.replace("{tag}", blackList), "0", ConditionType.EQUAL);
                fieldConditions.add(fieldCondition);
            }
        }
        if(StringUtils.isNotBlank(sex)){
            fieldCondition = new FieldCondition(Constants.SEX, sex, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        
        if(StringUtils.isNotBlank(birthYear)){
            fieldCondition = new FieldCondition(Constants.BIRTH_YEAR, birthYear, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        return fieldConditions;
    }
    /**
     * @return the {@link #ecQueryDtos}
     */
    public List<EcQueryDto> getEcQueryDtos() {
        return ecQueryDtos;
    }
    /**
     * @param ecQueryDtos
     * the {@link #ecQueryDtos} to set
     */
    public void setEcQueryDtos(List<EcQueryDto> ecQueryDtos) {
        this.ecQueryDtos = ecQueryDtos;
    }
    /**
     * @return the {@link #mediaQueryDtos}
     */
    public List<MediaQueryDto> getMediaQueryDtos() {
        return mediaQueryDtos;
    }
    /**
     * @param mediaQueryDtos
     * the {@link #mediaQueryDtos} to set
     */
    public void setMediaQueryDtos(List<MediaQueryDto> mediaQueryDtos) {
        this.mediaQueryDtos = mediaQueryDtos;
    }
    /**
     * @return the {@link #title}
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title
     * the {@link #title} to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the {@link #assetHouse}
     */
    public String getAssetHouse() {
        return assetHouse;
    }
    /**
     * @param assetHouse
     * the {@link #assetHouse} to set
     */
    public void setAssetHouse(String assetHouse) {
        this.assetHouse = assetHouse;
    }
    /**
     * @return the {@link #assetCar}
     */
    public String getAssetCar() {
        return assetCar;
    }
    /**
     * @param assetCar
     * the {@link #assetCar} to set
     */
    public void setAssetCar(String assetCar) {
        this.assetCar = assetCar;
    }
    /**
     * @return the {@link #assetFin}
     */
    public String getAssetFin() {
        return assetFin;
    }
    /**
     * @param assetFin
     * the {@link #assetFin} to set
     */
    public void setAssetFin(String assetFin) {
        this.assetFin = assetFin;
    }
    /**
     * @return the {@link #assetWealth}
     */
    public String getAssetWealth() {
        return assetWealth;
    }
    /**
     * @param assetWealth
     * the {@link #assetWealth} to set
     */
    public void setAssetWealth(String assetWealth) {
        this.assetWealth = assetWealth;
    }
    /**
     * @return the {@link #applyLoanCount}
     */
    public String getApplyLoanCount() {
        return applyLoanCount;
    }
    /**
     * @param applyLoanCount
     * the {@link #applyLoanCount} to set
     */
    public void setApplyLoanCount(String applyLoanCount) {
        this.applyLoanCount = applyLoanCount;
    }
    /**
     * @return the {@link #applyLoanOrgCount}
     */
    public String getApplyLoanOrgCount() {
        return applyLoanOrgCount;
    }
    /**
     * @param applyLoanOrgCount
     * the {@link #applyLoanOrgCount} to set
     */
    public void setApplyLoanOrgCount(String applyLoanOrgCount) {
        this.applyLoanOrgCount = applyLoanOrgCount;
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
     * @return the {@link #type}
     */
    public String getType() {
        return type;
    }
    /**
     * @param type
     * the {@link #type} to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the {@link #ecQueryJson}
     */
    public String getEcQueryJson() {
        return ecQueryJson;
    }
    /**
     * @param ecQueryJson
     * the {@link #ecQueryJson} to set
     */
    public void setEcQueryJson(String ecQueryJson) {
        this.ecQueryJson = ecQueryJson;
    }
    /**
     * @return the {@link #mediaQueryJson}
     */
    public String getMediaQueryJson() {
        return mediaQueryJson;
    }
    /**
     * @param mediaQueryJson
     * the {@link #mediaQueryJson} to set
     */
    public void setMediaQueryJson(String mediaQueryJson) {
        this.mediaQueryJson = mediaQueryJson;
    }

    /**
     * @return the {@link #brandListStr}
     */
    public String getBrandListStr() {
        return brandListStr;
    }

    /**
     * @param brandListStr
     * the {@link #brandListStr} to set
     */
    public void setBrandListStr(String brandListStr) {
        this.brandListStr = brandListStr;
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
    
}
