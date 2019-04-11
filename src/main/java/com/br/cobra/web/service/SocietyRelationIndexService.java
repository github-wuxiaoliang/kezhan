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
import com.br.cobra.web.model.SocietyRelationQueryDto;

@Service
public class SocietyRelationIndexService {
    private static final String INDEX_NAME = "society_relation_index";
    
    /**
     * 根据条件查询信息
     * @param airTravelQueryDto
     * @param currentPage
     * @param pageSize
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public PageDto<Map<String, Object>> search(SocietyRelationQueryDto societyRelationQueryDto,int currentPage,int pageSize) throws QueryParseException, QueryException{
        List<FieldCondition> fieldConditions = societyRelationQueryDto.generateFieldCondition();
        Query query = new LuceneQuery(INDEX_NAME, fieldConditions);
        PageDto<Map<String, Object>> pageDto =  query.search(currentPage, pageSize);
        
        List<Map<String, Object>> detail = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> singleRecord : pageDto.getResult()){
            detail.add(convertMapObject(singleRecord));
        }

        pageDto.setResult(detail);
        return pageDto;
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
     * @param airTravelQueryDto
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public int count(SocietyRelationQueryDto societyRelationQueryDto) throws QueryParseException, QueryException{
        List<FieldCondition> fieldConditions = societyRelationQueryDto.generateFieldCondition();
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
        StringBuilder tagBuilder = new StringBuilder();
        StringBuilder industryBuilder = new StringBuilder();
        for(Entry<String, Object> entry : data.entrySet()){
            if(entry.getKey().startsWith("sr_int_tag")){
                tagBuilder.append(entry.getValue()+";");
            }else if(entry.getKey().startsWith("sr_industry")){
                industryBuilder.append(entry.getValue()+";");
            }else{
                result.put(entry.getKey(), entry.getValue());
            }
        }
        String tagStr = tagBuilder.toString();
        if(StringUtils.isNotBlank(tagStr)){
            tagStr = tagStr.substring(0, tagStr.length() - 1);
            result.put("sr_int_tag", tagStr);
        }
        String industryStr = industryBuilder.toString();
        if(StringUtils.isNotBlank(industryStr)){
            industryStr = industryStr.substring(0, industryStr.length() - 1);
            result.put("sr_industry", industryStr);
        }
        return result;
    }

}
