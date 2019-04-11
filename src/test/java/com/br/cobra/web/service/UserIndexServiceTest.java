package com.br.cobra.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.br.cobra.query.exception.QueryException;
import com.br.cobra.query.exception.QueryParseException;
import com.br.cobra.query.model.PageDto;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.model.EcQueryDto;
import com.br.cobra.web.model.UserIndexQueryDto;

public class UserIndexServiceTest extends BaseTest{
    @Autowired
    private UserIndexService userIndexService;
    
    @Test
    public void testSearch() throws QueryParseException, QueryException{
        UserIndexQueryDto userIndexQueryDto = new UserIndexQueryDto();
        EcQueryDto ecQueryDtos = new EcQueryDto();
        ecQueryDtos.setCateName("家用电器");
        ecQueryDtos.setVisit("1-5");
        List<EcQueryDto> list  = new ArrayList<EcQueryDto>();
        list.add(ecQueryDtos);
        
        userIndexQueryDto.setEcQueryDtos(list);
        long startTime1 = System.currentTimeMillis();
        userIndexService.count(userIndexQueryDto);
        long endTime1 = System.currentTimeMillis();
        System.out.println("count cost time is "+ ( endTime1 - startTime1 ));
        
        long startTime = System.currentTimeMillis();
        PageDto<Map<String, Object>> page = userIndexService.search(userIndexQueryDto, 20, 1000);
        long endTime = System.currentTimeMillis();
        System.out.println("cost time is "+(endTime - startTime));
        
        
        System.out.println(JSON.toJSONString(page.getResult().get(3).get(Constants.KEY_ID)));
    }

}
 