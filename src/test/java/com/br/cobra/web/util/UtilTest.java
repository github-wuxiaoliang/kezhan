package com.br.cobra.web.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.web.constant.Constants;

public class UtilTest {

    @Test
    public void testDecode() throws UnsupportedEncodingException {
        System.out.println(URLDecoder.decode("中文", "utf-8"));
    }

    @Test
    public void loadLibraryFromJar() throws IOException {
        String path="/resources/jniidcode.so";
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The path to be absolute (start with '/').");
        }

        String[] parts = path.split("/");
        String filename = parts.length > 1 ? parts[(parts.length - 1)] : null;

        String prefix = "";
        String suffix = null;
        if (filename != null) {
            parts = filename.split("\\.", 2);
            prefix = parts[0];
            suffix = parts.length > 1 ? "." + parts[(parts.length - 1)] : null;
        }

        if ((filename == null) || (prefix.length() < 3)) {
            throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
        }

        File temp = File.createTempFile(prefix, suffix);
        System.out.println(temp.getAbsolutePath());
    }

    @Test
    public void testStringReplace(){
        String str = "^aaab_*$";
        System.out.println(str.replace("^", "").replace("_*$", ""));
        
    }
    
    @Test
    public void generateUserDataHeadTitle(){
        String[] ecCateArray = PropertiesUtil.getStringValue("ec_cate").split(",");
        String[] mediaCateArray = PropertiesUtil.getStringValue("media_cate").split(",");
        List<String> headTitle = new ArrayList<String>();
        
        headTitle.add("uniq_key");
        headTitle.add("key");
        headTitle.add("name");
        headTitle.add("出生年");
        headTitle.add("性别");
        headTitle.add("企业主");
        headTitle.add("是否有房");
        headTitle.add("是否有车");
        headTitle.add("是否理财");
        headTitle.add("客户价值");
        
        headTitle.add("多次申请总次数");
        headTitle.add("多次申请机构数");
        headTitle.add("归属地-省");
        headTitle.add("归属地-市");
        headTitle.add("品牌偏好");
        headTitle.add("特殊名单");
        
        for(String ecCate : ecCateArray){
            headTitle.add(Constants.EC_CATE_VISIT.replace("{cate}", ecCate));
            headTitle.add(Constants.EC_CATE_NUM.replace("{cate}", ecCate));
            headTitle.add(Constants.EC_CATE_PAY.replace("{cate}", ecCate));
            headTitle.add(Constants.EC_CATE_LAST_MONTH.replace("{cate}", ecCate));
        }
        
        for(String mediaCate : mediaCateArray){
            headTitle.add(Constants.MEDIA_CATE_VISITDAY.replace("{cate}", mediaCate));
            headTitle.add(Constants.MEDIA_CATE_LAST_MONTH.replace("{cate}", mediaCate));
        }
        System.out.println(JSON.toJSON(headTitle));
    }
    
    @Test
    public void testUserDataHeadAttr(){
        String[] ecCateArray = PropertiesUtil.getStringValue("ec_cate").split(",");
        String[] mediaCateArray = PropertiesUtil.getStringValue("media_cate").split(",");
        List<String> headAttr = new ArrayList<String>();
        
        headAttr.add(Constants.KEY_ID);
        headAttr.add(Constants.NAME);
        headAttr.add(Constants.BIRTH_YEAR);
        headAttr.add(Constants.SEX);
        headAttr.add(Constants.TITLE);
        headAttr.add(Constants.ASSET_HOUSE);
        headAttr.add(Constants.ASSET_CAR);
        headAttr.add(Constants.ASSET_FIN);
        headAttr.add(Constants.ASSET_WEALTH);
        
        headAttr.add(Constants.APPLY_LOAN_COUNT);
        headAttr.add(Constants.APPLY_LOAN_ORG_COUNT);
        headAttr.add(Constants.AREA_PROVINCE);
        headAttr.add(Constants.AREA_CITY);
        headAttr.add("^cf\\|brand_[^_]*$");
        headAttr.add("^cf\\|black_list_[^_]*$");
        
        for(String ecCate : ecCateArray){
            headAttr.add(Constants.EC_CATE_VISIT.replace("{cate}", ecCate));
            headAttr.add(Constants.EC_CATE_NUM.replace("{cate}", ecCate));
            headAttr.add(Constants.EC_CATE_PAY.replace("{cate}", ecCate));
            headAttr.add(Constants.EC_CATE_LAST_MONTH.replace("{cate}", ecCate));
        }
        
        for(String mediaCate : mediaCateArray){
            headAttr.add(Constants.MEDIA_CATE_VISITDAY.replace("{cate}", mediaCate));
            headAttr.add(Constants.MEDIA_CATE_LAST_MONTH.replace("{cate}", mediaCate));
        }
        System.out.println(JSON.toJSONString(headAttr));
    }
    
    @Test
    public void testUserDataDefaultVal(){
        String[] ecCateArray = PropertiesUtil.getStringValue("ec_cate").split(",");
        String[] mediaCateArray = PropertiesUtil.getStringValue("media_cate").split(",");
        List<String> defaultValue = new ArrayList<String>();
        
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        
        for(@SuppressWarnings("unused") String ecCate : ecCateArray){
            defaultValue.add("");
            defaultValue.add("");
            defaultValue.add("");
            defaultValue.add("");
        }
        
        for(@SuppressWarnings("unused") String mediaCate : mediaCateArray){
            defaultValue.add("");
            defaultValue.add("");
        }
        System.out.println(JSON.toJSONString(defaultValue));
    }
    
    @Test
    public void generateAirTravelHeadTitle(){
        List<String> headTitle = new ArrayList<String>();
        headTitle.add(Constants.KEY_ID);
        headTitle.add(Constants.KEY_CELL);
        headTitle.add(Constants.NAME);
        headTitle.add(Constants.BIRTH_YEAR);
        headTitle.add(Constants.SEX);
        headTitle.add(Constants.TRAVEL_HAS_CELL);
        headTitle.add(Constants.TRAVEL_MONTH);
        headTitle.add("black_list");
        
        for(int i = 0 ; i< 3; i++){
            String quarter = "m6";
            if(i==1){
                quarter = "m12";
            }else if(i==2){
                quarter = "m24";
            }
            
            headTitle.add(Constants.TRAVEL_QUARTER_TOTAL_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_FIRST_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_BUSINESS_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_ECONOMY_NUM.replace("{QUARTER}", quarter));
        }
            
        for(int i = 0; i < 5; i++){
            String quarter = "q"+i;
            headTitle.add(Constants.TRAVEL_QUARTER_TOTAL_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_FIRST_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_BUSINESS_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_ECONOMY_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.TRAVEL_QUARTER_MOST_CITY.replace("{QUARTER}", quarter));
        }
        
        System.out.println(JSON.toJSONString(headTitle).replace("\"", "").replace("[", "").replace("]", ""));
    }
    
    @Test
    public void generateAirTravelDefaultVal(){
        List<String> defaultValue = new ArrayList<String>();
        
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        
        for(int i = 0 ; i< 3;i++){
            defaultValue.add("0");
            defaultValue.add("0");
            defaultValue.add("0");
            defaultValue.add("0");
        }
        
        for(int i = 0; i < 5; i++){
            defaultValue.add("0");
            defaultValue.add("0");
            defaultValue.add("0");
            defaultValue.add("0");
            defaultValue.add("");
        }
        System.out.println(JSON.toJSONString(defaultValue).replace("\"", "").replace("[", "").replace("]", ""));
    }

    @Test
    public void generateAccountChangeHeadTitle(){
        List<String> headTitle = new ArrayList<String>();
        
        headTitle.add(Constants.KEY_CELL);
        headTitle.add(Constants.NAME);
        headTitle.add(Constants.BIRTH_YEAR);
        headTitle.add(Constants.SEX);
        headTitle.add(Constants.ACM_CARD_INDEX);
        headTitle.add(Constants.ACM_QUERY_MONTH);
        headTitle.add(Constants.ACM_AVG_CREDIT_OUT);
        headTitle.add(Constants.ACM_AVG_DEBIT_IN);
        headTitle.add(Constants.AREA_PROVINCE);
        headTitle.add(Constants.AREA_CITY);
        headTitle.add("black_list");
        
        for(int i = 0; i <= 5; i++){
            String quarter = "m"+(i+1);
            headTitle.add(Constants.ACM_QUARTER_DEBIT_BALANCE.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_INVEST.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_OUT.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_OUT_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_REPAY.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_IN.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_IN_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_OUT.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_OUT_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_CASH.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_IN.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_IN_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_DEF.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_LOAN.replace("{QUARTER}", quarter));
            
            headTitle.add(Constants.ACM_QUARTER_CONS.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_MAX_IN.replace("{QUARTER}", quarter));
        }
        
        for(int i = 0; i <= 5; i++){
            String quarter = "m"+(i*3+1)+"_m"+(i*3+3);
            headTitle.add(Constants.ACM_QUARTER_DEBIT_BALANCE.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_INVEST.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_OUT.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_OUT_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_REPAY.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_IN.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_DEBIT_IN_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_OUT.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_OUT_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_CASH.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_IN.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_IN_NUM.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CREDIT_DEF.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_LOAN.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_CONS.replace("{QUARTER}", quarter));
            headTitle.add(Constants.ACM_QUARTER_MAX_IN.replace("{QUARTER}", quarter));
        }
        
        System.out.println(JSON.toJSONString(headTitle).replace("\"", "").replace("[", "").replace("]", ""));
    }
    
    @Test
    public void generateAccountChangeData(){
        List<String> defaultValue = new ArrayList<String>();
        
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        
        for(int i = 0; i < 12 * 16; i++){
            defaultValue.add("");
        }
        System.out.println(JSON.toJSONString(defaultValue).replace("\"", "").replace("[", "").replace("]", ""));
    }
    
    @Test
    public void generateWeiboHeadTitle(){
        List<String> headTitle = new ArrayList<String>();
        
        headTitle.add(Constants.SR_UID);
        headTitle.add(Constants.KEY_CELL);
        headTitle.add(Constants.KEY_MAIL);
        headTitle.add("black_list");
        headTitle.add(Constants.SR_MATCH_TYPE);
        headTitle.add(Constants.SR_USER_TYPE);
        headTitle.add(Constants.SR_GENDER);
        
        headTitle.add(Constants.SR_REG_DATE);
        headTitle.add(Constants.SR_BIRTHDAY);
        headTitle.add("sr_int_tag");
        headTitle.add(Constants.SR_TALENT);
        headTitle.add(Constants.SR_LEVEL);
        headTitle.add(Constants.SR_FOLLOW_NUM);
        headTitle.add(Constants.SR_FANS_NUM);
        headTitle.add(Constants.SR_SPREAD);
        
        headTitle.add("sr_industry");
        headTitle.add(Constants.SR_LOCATION);
        headTitle.add(Constants.SR_WEIBO_NUM);
        headTitle.add(Constants.SR_NICK);
        headTitle.add(Constants.SR_BLOG);
        headTitle.add(Constants.SR_DOMAIN);
        
        System.out.println(JSON.toJSONString(headTitle).replace("\"", "").replace("[", "").replace("]", ""));
    }
    
    @Test
    public void generateWeiboDefaultVal(){
        List<String> defaultValue = new ArrayList<String>();
        
        for(int i = 0; i < 21;i++){
            defaultValue.add("");
        }
        System.out.println(JSON.toJSONString(defaultValue).replace("\"", "").replace("[", "").replace("]", ""));

    }

    @Test
    public void generateApplyLoanHeadTitle(){
        List<String> headTitle = new ArrayList<String>();
        
        headTitle.add(Constants.KEY_ID);
        headTitle.add(Constants.APPLY_LOAN_COUNT);
        headTitle.add(Constants.APPLY_LOAN_ORG_COUNT);
        headTitle.add(Constants.APPLY_LOAN_LAST_MONTH);
        headTitle.add("black_list");
        headTitle.add("apply_org");
        headTitle.add("apply_org_type");
        
        for(int i = 0 ; i < 4;i++){
            String quarter = "total_";
            
            if(i == 1){
                quarter = "m3_";
            }else if(i== 2){
                quarter = "m6_";
            }else if(i== 3){
                quarter = "m12_";
            }
            
            headTitle.add(quarter+"bank_allnum");
            headTitle.add(quarter+"bank_orgnum");
            headTitle.add(quarter+"not_bank_2_allnum");
            headTitle.add(quarter+"not_bank_2_orgnum");
            headTitle.add(quarter+"not_bank_3_allnum");
            headTitle.add(quarter+"not_bank_3_orgnum");
            headTitle.add(quarter+"not_bank_4_allnum");
            headTitle.add(quarter+"not_bank_4_orgnum");
            headTitle.add(quarter+"not_bank_6_allnum");
            headTitle.add(quarter+"not_bank_6_orgnum");
            headTitle.add(quarter+"not_bank_10_allnum");
            headTitle.add(quarter+"not_bank_10_orgnum");
            headTitle.add(quarter+"not_bank_11_allnum");
            headTitle.add(quarter+"not_bank_11_orgnum");
            headTitle.add(quarter+"not_bank_12_allnum");
            headTitle.add(quarter+"not_bank_12_orgnum");
            headTitle.add(quarter+"not_bank_13_allnum");
            headTitle.add(quarter+"not_bank_13_orgnum");
            headTitle.add(quarter+"not_bank_14_allnum");
            headTitle.add(quarter+"not_bank_14_orgnum");
            headTitle.add(quarter+"not_bank_15_allnum");
            headTitle.add(quarter+"not_bank_15_orgnum");
            headTitle.add(quarter+"not_bank_16_allnum");
            headTitle.add(quarter+"not_bank_16_orgnum");
        }
        System.out.println(JSON.toJSONString(headTitle).replace("\"", "").replace("[", "").replace("]", ""));

    }
    
    @Test
    public void generateDefaultVal(){
        
        List<String> defaultValue = new ArrayList<String>();
        for(int i = 0 ; i< 7;i++){
            defaultValue.add("");
        }
        for(int i = 0 ; i < 24*4;i++){
            defaultValue.add("0");
        }
        System.out.println(JSON.toJSONString(defaultValue).replace("\"", "").replace("[", "").replace("]", ""));
    }
}

