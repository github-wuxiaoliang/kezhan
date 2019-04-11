package com.br.cobra.web.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;

/**
 * 多次申请查询类
 * @author chun
 *
 */
public class ApplyLoanQueryDto {
	private static final String MAXBETWEENVALUE = "1-"+Integer.MAX_VALUE;
	private String count;
	private String orgCount;
	private String type;
	private String queryMonth;
	private String applyLoanType;
	private String applyOrgs;
	private String blackListStr;
	
	
	public List<FieldCondition> generateFieldCondition(){
        List<FieldCondition> fieldConditions = new ArrayList<FieldCondition>();
        
        FieldCondition fieldCondition = null;
        if(StringUtils.isNotBlank(type)){
        	fieldCondition = new FieldCondition(Constants.KEY_TYPE, type, ConditionType.EQUAL);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(queryMonth)){
        	fieldCondition = new FieldCondition(Constants.APPLY_LOAN_LAST_MONTH, queryMonth, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(applyLoanType)){
        	fieldCondition = new FieldCondition(Constants.APPLY_LOAN_SINGLE_TYPE_NUM.replace("{TYPE}", applyLoanType), MAXBETWEENVALUE, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(applyOrgs)){
        	for(String org : applyOrgs.split(",")){
        		fieldCondition = new FieldCondition(Constants.APPLY_LOAN_SINGLE_ORG_NUM.replace("{APICODE}", org), MAXBETWEENVALUE, ConditionType.BETWEEN);
                fieldConditions.add(fieldCondition);
        	}
        }
        
        // applyloan
        if(StringUtils.isNotBlank(count)){
            fieldCondition = new FieldCondition(
                Constants.APPLY_LOAN_COUNT, count, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        if(StringUtils.isNotBlank(orgCount)){
            fieldCondition = new FieldCondition(
                Constants.APPLY_LOAN_ORG_COUNT, orgCount, ConditionType.BETWEEN);
            fieldConditions.add(fieldCondition);
        }
        
        if(StringUtils.isNotBlank(blackListStr)){
        	for(String blackList : blackListStr.split(",")){
                fieldCondition = new FieldCondition(
                    Constants.BLACK_LIST_TAG.replace("{tag}", blackList), "0", ConditionType.EQUAL);
                fieldConditions.add(fieldCondition);
            }
        }
        
        
        return fieldConditions;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	/**
	 * @return the applyLoanType
	 */
	public String getApplyLoanType() {
		return applyLoanType;
	}
	/**
	 * @param applyLoanType the applyLoanType to set
	 */
	public void setApplyLoanType(String applyLoanType) {
		this.applyLoanType = applyLoanType;
	}
	/**
	 * @return the applyOrgs
	 */
	public String getApplyOrgs() {
		return applyOrgs;
	}
	/**
	 * @param applyOrgs the applyOrgs to set
	 */
	public void setApplyOrgs(String applyOrgs) {
		this.applyOrgs = applyOrgs;
	}
	/**
	 * @return the blackListStr
	 */
	public String getBlackListStr() {
		return blackListStr;
	}
	/**
	 * @param blackListStr the blackListStr to set
	 */
	public void setBlackListStr(String blackListStr) {
		this.blackListStr = blackListStr;
	}
	/**
	 * @return the count
	 */
	public String getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}
	/**
	 * @return the orgCount
	 */
	public String getOrgCount() {
		return orgCount;
	}
	/**
	 * @param orgCount the orgCount to set
	 */
	public void setOrgCount(String orgCount) {
		this.orgCount = orgCount;
	}
	
	
	
}
