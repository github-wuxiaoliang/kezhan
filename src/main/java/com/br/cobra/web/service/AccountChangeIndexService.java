package com.br.cobra.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.br.cobra.query.LuceneQuery;
import com.br.cobra.query.Query;
import com.br.cobra.query.exception.QueryException;
import com.br.cobra.query.exception.QueryParseException;
import com.br.cobra.query.model.ConditionType;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.query.model.PageDto;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.model.AccountChangeQueryDto;

@Service
public class AccountChangeIndexService {
    private static final String INDEX_NAME = "account_change_index";
    
    /**
     * 根据条件查询信息
     * @param airTravelQueryDto
     * @param currentPage
     * @param pageSize
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public PageDto<Map<String, Object>> search(AccountChangeQueryDto accountChangeQueryDto,int currentPage,int pageSize) throws QueryParseException, QueryException{
        List<FieldCondition> fieldConditions = accountChangeQueryDto.generateFieldCondition();
        Query query = new LuceneQuery(INDEX_NAME, fieldConditions);
        PageDto<Map<String, Object>> pageDto =  query.search(currentPage, pageSize);

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
        return detail;
    }
    
    /**
     * 查询满足条件用户总数
     * @param airTravelQueryDto
     * @return
     * @throws QueryParseException
     * @throws QueryException
     */
    public int count(AccountChangeQueryDto accountChangeQueryDto) throws QueryParseException, QueryException{
        List<FieldCondition> fieldConditions = accountChangeQueryDto.generateFieldCondition();
        Query query = new LuceneQuery(INDEX_NAME, fieldConditions);
        
        return query.count();
    }

}
