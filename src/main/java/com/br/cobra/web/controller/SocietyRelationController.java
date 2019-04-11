package com.br.cobra.web.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.bfd.enctype.Decode;
import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.query.model.PageDto;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.constant.HbaseDescCache;
import com.br.cobra.web.model.HbaseIndexShowDesc;
import com.br.cobra.web.model.ResponseDto;
import com.br.cobra.web.model.SocietyRelationQueryDto;
import com.br.cobra.web.model.UserInfoDto;
import com.br.cobra.web.service.SocietyRelationIndexService;

@Controller
@RequestMapping("/society_relation")
public class SocietyRelationController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(SocietyRelationController.class);
    @Autowired
    private SocietyRelationIndexService societyRelationIndexService;
    private static final int MAX_EXPORT_DATA = NumberUtils.toInt(PropertiesUtil.getStringValue("export.max.data.count"),100000);
    private static final int DEFAULT_EXPORT_PAGE_SIZE = NumberUtils.toInt(PropertiesUtil.getStringValue("export.page.size"),10000);
    private static final String[] EXPORT_DECODE_USER_ARRAY = PropertiesUtil.getStringValue("export.decode.user").split(",");
    private static final int DEFAULT_EXPORT_MAX_RANDOM_POS = NumberUtils.toInt(PropertiesUtil.getStringValue("export.society.relation.max.random.pos"),100000);
    private static final int DEFAULT_LOGGER_TIME = 1000;
    
    @RequestMapping(value="list",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request,SocietyRelationQueryDto societyRelationQueryDto,
                             @RequestParam(value="currentPage",defaultValue="1")int currentPage){
        
        ModelAndView view = new ModelAndView("/societyRelation/list");
        view.getModel().put("ctx", request.getContextPath());
        List<HbaseIndexShowDesc> tableDescList = HbaseDescCache.getIndexShowDescList();
        view.getModel().put("tableDesc", tableDescList);
        view.getModel().put("tableDescJson", JSON.toJSONString(tableDescList).replace("\"", "\\\""));

        return view;
    }
    
    @RequestMapping(value="generateQueryCondition",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseDto<String> generateQueryCondition(SocietyRelationQueryDto societyRelationQueryDto){
        StringBuilder builder = new StringBuilder();
        try{
            List<FieldCondition> conditions = societyRelationQueryDto.generateFieldCondition();
            
            for( int i = 0; i < conditions.size(); i++ ){
                
                FieldCondition fieldCondition = conditions.get(i);
                if(i >= 1){
                    builder.append(" and ");
                }
                switch (fieldCondition.getConditionType()) {
                    case BETWEEN:
                        String[] values = fieldCondition.getValue().split("-");
                        builder.append(Constants.HBASE_DATA_COL).append(Constants.INDEX_FAMILY_COL_SEPATOR).append(fieldCondition.getFieldName()).append(" ").append(" between ");
                        builder.append(values[0]).append(" and ").append(values[1]);
                        break;
                    case EQUAL:
                        builder.append(Constants.HBASE_DATA_COL).append(Constants.INDEX_FAMILY_COL_SEPATOR).append(fieldCondition.getFieldName()).append("=").append(fieldCondition.getValue());
                    default:
                        break;
                }
            }
        }catch(Exception e){
            return new ResponseDto<>(400, "生成查询sql条件失败.");
        }
        
        return new ResponseDto<String>(builder.toString());
        
    }

    /**
     * 查询数据详情
     * @param keyId
     * @return
     */
    @RequestMapping(value="detail",method={RequestMethod.GET})
    @ResponseBody
    public ResponseDto<Map<String,Object>> detail(@RequestParam(value="docId")String docId){
        long startTime = System.currentTimeMillis();
        if(StringUtils.isBlank(docId)){
            return new ResponseDto<Map<String,Object>>(new HashMap<String,Object>());
        }
        try{
            Map<String, Object> data = societyRelationIndexService.getDetailByDocId(NumberUtils.toInt(docId));
            if(data != null){
                return new ResponseDto<Map<String,Object>>(data);
            }else{
                return new ResponseDto<>(400, "无对应航旅信息.");
            }
        }catch(Exception e){
            LOGGER.error("查询详情失败."+e.getMessage(),e);
            return new ResponseDto<>(400, e.getMessage());
        }finally{
            long endTime = System.currentTimeMillis();
            if(endTime - startTime > DEFAULT_LOGGER_TIME){
                LOGGER.info("detail cost time is "+(endTime - startTime));
            }
        }
    }
    
    /**
     * 导出满足条件数据
     * @param request
     * @param response
     * @param userIndexQueryDto
     */
    @RequestMapping(value="export",method={RequestMethod.GET,RequestMethod.POST})
    public void exportData(HttpServletRequest request, HttpServletResponse response ,SocietyRelationQueryDto societyRelationQueryDto){
        List<Map<String, Object>> exportData = new ArrayList<Map<String,Object>>();
        long startTime = System.currentTimeMillis();
        
        int count = 0;
        int searchPage = 1;
        boolean isDecode = false;
        try{
            
            // 判断是否需要解密数据
            UserInfoDto user = (UserInfoDto) request.getSession().getAttribute(Constants.SESSION_USER_INFO);
            for(String decodeUserId : EXPORT_DECODE_USER_ARRAY){
                if(decodeUserId.equals(""+user.getUserId())){
                    isDecode = true;
                    break;
                }
            }
            
            // decode查询条件
            societyRelationQueryDto.setBlackListStr(URLDecoder.decode(societyRelationQueryDto.getBlackListStr(), "utf-8"));
            societyRelationQueryDto.setTagStr(URLDecoder.decode(societyRelationQueryDto.getTagStr(), "utf-8"));
            societyRelationQueryDto.setIndustryStr(URLDecoder.decode(societyRelationQueryDto.getIndustryStr(), "utf-8"));
            societyRelationQueryDto.setLocation(URLDecoder.decode(societyRelationQueryDto.getLocation(),"utf-8"));
            
            int randomStartPos = 0;
            int currentPos = 0;
            
            // 计算随机查询起始查询位置
            int totalRecord =  societyRelationIndexService.count(societyRelationQueryDto);
            if(totalRecord > MAX_EXPORT_DATA){
                int randMaxPos = totalRecord  - MAX_EXPORT_DATA;
                if(randMaxPos > DEFAULT_EXPORT_MAX_RANDOM_POS){
                    randMaxPos = DEFAULT_EXPORT_MAX_RANDOM_POS;
                }
                
                randomStartPos = RandomUtils.nextInt(randMaxPos);
                searchPage = randomStartPos / DEFAULT_EXPORT_PAGE_SIZE+1;
                currentPos = (searchPage - 1)*DEFAULT_EXPORT_PAGE_SIZE;
            }
            
            do{
                PageDto<Map<String, Object>> pageDto = societyRelationIndexService.search(societyRelationQueryDto, searchPage, DEFAULT_EXPORT_PAGE_SIZE);
                
                // 遍历结果,去除满足条件数据
                for(Map<String, Object> singleRecord : pageDto.getResult()){
                    // 解密keyId数据
                    if(isDecode){
                        if(singleRecord.containsKey(Constants.KEY_CELL) && StringUtils.isNotBlank(singleRecord.get(Constants.KEY_CELL).toString())){
                            String decodeKeyCell = Decode.toDecodeSimple(singleRecord.get(Constants.KEY_CELL).toString());
                            singleRecord.put(Constants.KEY_CELL, decodeKeyCell);
                        }
                        if(singleRecord.containsKey(Constants.KEY_MAIL) && StringUtils.isNotBlank(singleRecord.get(Constants.KEY_MAIL).toString())){
                            String decodeKeyMail = Decode.toDecodeSimple(singleRecord.get(Constants.KEY_MAIL).toString());
                            singleRecord.put(Constants.KEY_MAIL, decodeKeyMail);
                        }
                    }
                    
                    if(currentPos >= randomStartPos){
                        exportData.add(singleRecord);
                        count++;
                    }
                    currentPos++;
                    if(count == MAX_EXPORT_DATA){
                        break;
                    }
                }
                
                searchPage++;
                // 判断数据是否已取完
                if(currentPos == pageDto.getTotalRecord()){
                    break;
                }
                
            }while(count < MAX_EXPORT_DATA);
            String[] headTitle = generateExportHeadTitle().toArray(new String[0]);
            String[] headAttr = generateExportHeadAttr().toArray(new String[0]);
            String[] defaultValue = generateExportDataDefaultValue().toArray(new String[0]);
            String fileName = "society_relation_data_"+System.currentTimeMillis()+".csv";
            
            exportDataToCsv(response, fileName, headTitle, headAttr, defaultValue, exportData);
        }catch(Exception e){
            LOGGER.error("导出数据失败."+e.getMessage(),e);
        }finally{
            long endTime = System.currentTimeMillis();
            if(endTime - startTime > DEFAULT_LOGGER_TIME){
                LOGGER.info("export cost time is "+(endTime - startTime));
            }
        }
        
    }
    
    private List<String> generateExportHeadTitle(){
        List<String> headTitle = new ArrayList<String>();
        
        headTitle.add(Constants.SR_UID);
        headTitle.add(Constants.KEY_CELL);
        headTitle.add(Constants.KEY_MAIL);
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
        
        return headTitle;
    }
    
    private List<String> generateExportHeadAttr(){
        List<String> headAttr = new ArrayList<String>();
        
        headAttr.add(Constants.SR_UID);
        headAttr.add(Constants.KEY_CELL);
        headAttr.add(Constants.KEY_MAIL);
        headAttr.add(Constants.SR_MATCH_TYPE);
        headAttr.add(Constants.SR_USER_TYPE);
        headAttr.add(Constants.SR_GENDER);
        
        headAttr.add(Constants.SR_REG_DATE);
        headAttr.add(Constants.SR_BIRTHDAY);
        headAttr.add("sr_int_tag");
        headAttr.add(Constants.SR_TALENT);
        headAttr.add(Constants.SR_LEVEL);
        headAttr.add(Constants.SR_FOLLOW_NUM);
        headAttr.add(Constants.SR_FANS_NUM);
        headAttr.add(Constants.SR_SPREAD);
        
        headAttr.add("sr_industry");
        headAttr.add(Constants.SR_LOCATION);
        headAttr.add(Constants.SR_WEIBO_NUM);
        headAttr.add(Constants.SR_NICK);
        headAttr.add(Constants.SR_BLOG);
        headAttr.add(Constants.SR_DOMAIN);
        return headAttr;
    }
    
    private List<String> generateExportDataDefaultValue(){
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
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        defaultValue.add("");
        
        return defaultValue;
    }
    
}
