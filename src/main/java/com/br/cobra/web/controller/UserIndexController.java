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
import com.alibaba.fastjson.TypeReference;
import com.bfd.enctype.Decode;
import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.query.model.PageDto;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.constant.HbaseDescCache;
import com.br.cobra.web.model.EcQueryDto;
import com.br.cobra.web.model.HbaseIndexShowDesc;
import com.br.cobra.web.model.MediaQueryDto;
import com.br.cobra.web.model.ResponseDto;
import com.br.cobra.web.model.UserIndexQueryDto;
import com.br.cobra.web.model.UserInfoDto;
import com.br.cobra.web.service.UserIndexService;

@Controller
@RequestMapping("/user_index")
public class UserIndexController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserIndexController.class);
    @Autowired
    private UserIndexService userIndexService;
    private static final int MAX_EXPORT_DATA = NumberUtils.toInt(PropertiesUtil.getStringValue("export.max.data.count"),100000);
    private static final int DEFAULT_EXPORT_PAGE_SIZE = NumberUtils.toInt(PropertiesUtil.getStringValue("export.page.size"),10000);
    private static final String[] EXPORT_DECODE_USER_ARRAY = PropertiesUtil.getStringValue("export.decode.user").split(",");
    private static final int DEFAULT_EXPORT_MAX_RANDOM_POS = NumberUtils.toInt(PropertiesUtil.getStringValue("export.user.max.random.pos"),100000);
    private static final int DEFAULT_LOGGER_TIME = 1000;
    
    @RequestMapping(value="list",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request,UserIndexQueryDto userIndexQueryDto,
                             @RequestParam(value="currentPage",defaultValue="1")int currentPage){
        
        ModelAndView view = new ModelAndView("/userIndex/list");
        
        view.getModel().put("ctx", request.getContextPath());
        List<HbaseIndexShowDesc> tableDescList = HbaseDescCache.getIndexShowDescList();
        view.getModel().put("tableDesc", tableDescList);
        view.getModel().put("tableDescJson", JSON.toJSONString(tableDescList).replace("\"", "\\\""));

        return view;
    }
    
    /**
     * 生成sql查询条件
     * @param userIndexQueryDto
     * @return
     */
    @RequestMapping(value="generateQueryCondition",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseDto<String> generateQueryCondition(UserIndexQueryDto userIndexQueryDto){
        StringBuilder builder = new StringBuilder();
        try{
            // 转换ec 查询条件
            if(StringUtils.isNotBlank(userIndexQueryDto.getEcQueryJson())){
                String ecQueryJson = URLDecoder.decode(userIndexQueryDto.getEcQueryJson(),"utf-8");
                List<EcQueryDto> ecQueryDtos = JSON.parseObject(ecQueryJson, new TypeReference<List<EcQueryDto>>(){});
                userIndexQueryDto.setEcQueryDtos(ecQueryDtos);
            }
            // 转换media 查询条件
            if(StringUtils.isNotBlank(userIndexQueryDto.getMediaQueryJson())){
                String mediaQueryJson = URLDecoder.decode(userIndexQueryDto.getMediaQueryJson(), "utf-8");
                List<MediaQueryDto> mediaQueryDtos = JSON.parseObject(
                    mediaQueryJson, new TypeReference<List<MediaQueryDto>>(){});
                userIndexQueryDto.setMediaQueryDtos(mediaQueryDtos);
            }
            
            userIndexQueryDto.setAreaProvince(URLDecoder.decode(userIndexQueryDto.getAreaProvince(), "utf-8"));
            userIndexQueryDto.setAreaCity(URLDecoder.decode(userIndexQueryDto.getAreaCity(), "utf-8"));
            userIndexQueryDto.setBrandListStr(URLDecoder.decode(userIndexQueryDto.getBrandListStr(), "utf-8"));
            userIndexQueryDto.setBlackListStr(URLDecoder.decode(userIndexQueryDto.getBlackListStr(), "utf-8"));
      
            List<FieldCondition> conditions = userIndexQueryDto.generateFieldCondition();
            
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
            Map<String, Object> data = userIndexService.getDetailByDocId(NumberUtils.toInt(docId));
            if(data != null){
                return new ResponseDto<Map<String,Object>>(data);
            }else{
                return new ResponseDto<>(400, "无对应用户信息.");
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
    public void exportData(HttpServletRequest request, HttpServletResponse response ,UserIndexQueryDto userIndexQueryDto){
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
            
            // 转换ec 查询条件
            if(StringUtils.isNotBlank(userIndexQueryDto.getEcQueryJson())){
                String ecQueryJson = URLDecoder.decode(userIndexQueryDto.getEcQueryJson(),"utf-8");
                List<EcQueryDto> ecQueryDtos = JSON.parseObject(ecQueryJson, new TypeReference<List<EcQueryDto>>(){});
                userIndexQueryDto.setEcQueryDtos(ecQueryDtos);
            }
            // 转换media 查询条件
            if(StringUtils.isNotBlank(userIndexQueryDto.getMediaQueryJson())){
                String mediaQueryJson = URLDecoder.decode(userIndexQueryDto.getMediaQueryJson(), "utf-8");
                List<MediaQueryDto> mediaQueryDtos = JSON.parseObject(
                    mediaQueryJson, new TypeReference<List<MediaQueryDto>>(){});
                userIndexQueryDto.setMediaQueryDtos(mediaQueryDtos);
            }
            
            userIndexQueryDto.setAreaProvince(URLDecoder.decode(userIndexQueryDto.getAreaProvince(), "utf-8"));
            userIndexQueryDto.setAreaCity(URLDecoder.decode(userIndexQueryDto.getAreaCity(), "utf-8"));
            userIndexQueryDto.setBrandListStr(URLDecoder.decode(userIndexQueryDto.getBrandListStr(), "utf-8"));
            userIndexQueryDto.setBlackListStr(URLDecoder.decode(userIndexQueryDto.getBlackListStr(), "utf-8"));
            
            int randomStartPos = 0;
            int currentPos = 0;
            
            // 计算随机查询起始查询位置
            int totalRecord =  userIndexService.count(userIndexQueryDto);
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
                PageDto<Map<String, Object>> pageDto = userIndexService.search(userIndexQueryDto, searchPage, DEFAULT_EXPORT_PAGE_SIZE);
                
                // 遍历结果,去除满足条件数据
                for(Map<String, Object> singleRecord : pageDto.getResult()){
                    // 解密keyId数据
                    if(isDecode){
                        String decodeKeyId = Decode.toDecodeSimple(singleRecord.get(Constants.KEY_ID).toString());
                        singleRecord.put(Constants.KEY_ID, decodeKeyId);
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
            String fileName = "data_"+System.currentTimeMillis()+".csv";
            
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
        String[] ecCateArray = PropertiesUtil.getStringValue("ec_cate").split(",");
        String[] mediaCateArray = PropertiesUtil.getStringValue("media_cate").split(",");
        List<String> headTitle = new ArrayList<String>();
        
        headTitle.add("key");
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
        }
        
        for(String mediaCate : mediaCateArray){
            headTitle.add(Constants.MEDIA_CATE_VISITDAY.replace("{cate}", mediaCate));
        }
        return headTitle;
    }
    
    private List<String> generateExportHeadAttr(){
        String[] ecCateArray = PropertiesUtil.getStringValue("ec_cate").split(",");
        String[] mediaCateArray = PropertiesUtil.getStringValue("media_cate").split(",");
        List<String> headAttr = new ArrayList<String>();
        
        headAttr.add(Constants.KEY_ID);
        headAttr.add(Constants.TITLE);
        headAttr.add(Constants.ASSET_HOUSE);
        headAttr.add(Constants.ASSET_CAR);
        headAttr.add(Constants.ASSET_FIN);
        headAttr.add(Constants.ASSET_WEALTH);
        
        headAttr.add(Constants.APPLY_LOAN_COUNT);
        headAttr.add(Constants.APPLY_LOAN_ORG_COUNT);
        headAttr.add(Constants.AREA_PROVINCE);
        headAttr.add(Constants.AREA_CITY);
        headAttr.add("brand");
        headAttr.add("black_list");
        
        for(String ecCate : ecCateArray){
            headAttr.add(Constants.EC_CATE_VISIT.replace("{cate}", ecCate));
            headAttr.add(Constants.EC_CATE_NUM.replace("{cate}", ecCate));
            headAttr.add(Constants.EC_CATE_PAY.replace("{cate}", ecCate));
        }
        
        for(String mediaCate : mediaCateArray){
            headAttr.add(Constants.MEDIA_CATE_VISITDAY.replace("{cate}", mediaCate));
        }
        return headAttr;
    }
    
    private List<String> generateExportDataDefaultValue(){
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
        
        for(@SuppressWarnings("unused") String ecCate : ecCateArray){
            defaultValue.add("");
            defaultValue.add("");
            defaultValue.add("");
        }
        
        for(@SuppressWarnings("unused") String mediaCate : mediaCateArray){
            defaultValue.add("");
        }
        return defaultValue;
    }
    
}
