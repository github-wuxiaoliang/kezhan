package com.br.cobra.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.br.cobra.query.LuceneQuery;
import com.br.cobra.query.Query;
import com.br.cobra.query.exception.QueryException;
import com.br.cobra.query.exception.QueryParseException;
import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.query.model.PageDto;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.model.UserIndexQueryDto;

@Service
public class UserIndexService {
    private static final String INDEX_NAME = "user_index";
    
    /**
     * 根据条件查询信息
     * @param userIndexQueryDto
     * @param currentPage
     * @param pageSize
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public PageDto<Map<String, Object>> search(UserIndexQueryDto userIndexQueryDto,int currentPage,int pageSize) throws QueryParseException, QueryException{
        List<FieldCondition> fieldConditions = userIndexQueryDto.generateFieldCondition();
        Query query = new LuceneQuery(INDEX_NAME, fieldConditions);
        PageDto<Map<String, Object>> pageDto =  query.search(currentPage, pageSize);
        
        List<Map<String, Object>> detail = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> data : pageDto.getResult()){
            detail.add(convertMapObject(data));
        }
        pageDto.setResult(detail);
        return pageDto;
    }
    
    /**
     * 获取单key数据详情
     * @param keyId
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public Map<String, Object> getDetailByKeyId(String keyId) throws QueryParseException, QueryException{
        List<FieldCondition> queryParam = new ArrayList<FieldCondition>();
        FieldCondition fieldCondition = new FieldCondition(
            Constants.KEY_ID, keyId, ConditionType.EQUAL);
        queryParam.add(fieldCondition);
        
        Query query = new LuceneQuery(INDEX_NAME, queryParam);
        
        Map<String, Object> detail = query.get();
        if(detail == null){
            return null;
        }
        return convertMapObject(detail);
    }
    
    /**
     * 根据docId查询文档信息
     * @param docId
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public Map<String, Object> getDetailByDocId(int docId) throws QueryParseException, QueryException{
        List<FieldCondition> queryParam = new ArrayList<FieldCondition>();
        FieldCondition fieldCondition = new FieldCondition(
            Constants.DOC_ID, "" + docId, ConditionType.EQUAL);
        queryParam.add(fieldCondition);
        
        Query query = new LuceneQuery(INDEX_NAME, queryParam);
        
        Map<String, Object> detail = query.get();
        if(detail == null){
            return null;
        }
        return convertMapObject(detail);
    }
    
    
    
    /**
     * 查询满足条件用户总数
     * @param userIndexQueryDto
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public int count(UserIndexQueryDto userIndexQueryDto) throws QueryParseException, QueryException{
        List<FieldCondition> fieldConditions = userIndexQueryDto.generateFieldCondition();
        Query query = new LuceneQuery(INDEX_NAME, fieldConditions);
        
        return query.count();
    }
    
    /**
     * 将品牌偏好数据转换成用逗号分隔
     * @param data
     * @return
     */
    private Map<String, Object> convertMapObject(Map<String, Object> data){
        Map<String, Object> result = new HashMap<String, Object>();
        StringBuilder builder = new StringBuilder();
        StringBuilder blackListBuilder = new StringBuilder();
        for(Entry<String, Object> entry : data.entrySet()){
            if(entry.getKey().startsWith("brand_")){
                builder.append(entry.getValue()+";");
            }else if(entry.getKey().startsWith("black_list_")){
                blackListBuilder.append(entry.getValue()+";");
            }else{
                result.put(entry.getKey(), entry.getValue());
            }
            
        }
        String brandStr = builder.toString();
        if(StringUtils.isNotBlank(brandStr)){
            brandStr = brandStr.substring(0, brandStr.length() - 1);
            result.put("brand", brandStr);
        }
        String blackListStr = blackListBuilder.toString();
        if(StringUtils.isNotBlank(blackListStr)){
            blackListStr = blackListStr.substring(0, blackListStr.length() - 1);
            result.put("black_list", blackListStr);
        }
        
        return result;
    }

}
