package com.br.cobra.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import com.br.cobra.common.util.MD5Utils;
import com.br.cobra.web.constant.HbaseDescCache;
import com.br.cobra.web.model.HbaseIndexExportDesc;
import com.br.cobra.web.model.HbaseIndexQueryDto;
import com.br.cobra.web.model.PageMoreDataDto;
import com.br.lightning_db.client.constant.Constants;
import com.br.lightning_db.client.exception.SqlExecuteException;
import com.br.lightning_db.client.hbase.HbaseClient;
import com.br.lightning_db.client.hbase.HbaseSqlClient;
import com.br.lightning_db.client.sql.SQLResult;
import com.br.lightning_db.sql_to_any.exception.SqlNotSupportException;
import com.br.lightning_db.sql_to_any.exception.SqlParseException;

@Service
public class HbaseIndexService {
    private ConcurrentHashMap<String, Long> countCacheMap = new ConcurrentHashMap<String, Long>();
    
    /**
     * 计算满足查询条件rowkey总数
     * @param query
     * @return
     * @throws SqlNotSupportException 
     * @throws SqlParseException 
     * @throws SqlExecuteException 
     */
    public long count(HbaseIndexQueryDto query) throws SqlExecuteException, SqlParseException, SqlNotSupportException{
        HbaseSqlClient client = new HbaseSqlClient();
        String countSql = query.getCountSql();
        String countSqlMd5Key = MD5Utils.cell32(countSql);
        
        if(countCacheMap.containsKey(countSqlMd5Key)){
            return countCacheMap.get(countSqlMd5Key);
        }
        SQLResult result = client.execSql(countSql);
        if(result == null || result.getData() == null || result.getData().isEmpty()){
            countCacheMap.put(countSqlMd5Key, 0l);
            return 0;
        }
        long count = (long) result.getData().get(0).get("count(*)");
        countCacheMap.put(countSqlMd5Key, count);
        return count;
    }
    
    /**
     * 分页加载数据详情
     * @param query
     * @return
     * @throws SqlExecuteException
     * @throws SqlParseException
     * @throws SqlNotSupportException
     */
    public PageMoreDataDto<Map<String, Object>> query(HbaseIndexQueryDto query) throws SqlExecuteException, SqlParseException, SqlNotSupportException{
        HbaseSqlClient client = new HbaseSqlClient();
        SQLResult sqlResult = client.execSql(query.getSelectSql(), query.getLimit(), query.getOffset());

        List<Map<String, Object>> detail = new ArrayList<Map<String, Object>>();
        if(sqlResult.getData() != null){
            for(Map<String, Object> data : sqlResult.getData()){
                detail.add(convertMapObject(data, query.getTable()));
            }
        }
        sqlResult.setData(detail);
        
        PageMoreDataDto<Map<String, Object>> result = new PageMoreDataDto<Map<String, Object>>(sqlResult.getData(), sqlResult.isHasMore());
        
        result.addExtra("offset", sqlResult.getOffset());
        return result;
    }
    
    /**
     * 根据rowkey查询单key详情
     * @param table
     * @param rowkey
     * @return
     * @throws IOException 
     */
    public Map<String, Object> detail(String table,String rowkey) throws IOException{
        Map<String, Object> result = new HashMap<String, Object>();
        Table htable = null;
        try{
            
            htable = HbaseClient.getInstance().getHtable(table);
            Get get = new Get(Bytes.toBytes(rowkey));

            Result hbaseResult = htable.get(get);
            if(!hbaseResult.isEmpty()){
                for(byte[]  familyByte: hbaseResult.getMap().keySet() ){
                    String family = Bytes.toString(familyByte);
                    NavigableMap<byte[], byte[]> qualifierMap = hbaseResult.getFamilyMap(familyByte);
                    
                    for (Entry<byte[], byte[]> entry : qualifierMap.entrySet()) {
                        String qualifier = Bytes.toString(entry.getKey());
                        String value = Bytes.toString(entry.getValue());
                        
                        result.put(family+Constants.FAMILY_QUALIER_SEAPTOR+qualifier, value);
                    }
                }
                
                // 进行数据合并
                result = convertMapObject(result, table);
            }
            
        }finally{
            // 释放htable资源
            HbaseClient.getInstance().relaseHtable(htable);
        }
        
        return result;
    }

    /**
     * 转换封装map数据
     * @param data
     * @param table
     * @return
     */
    private Map<String, Object> convertMapObject(Map<String, Object> data,String table){
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, StringBuilder> combineResultMap = new HashMap<String, StringBuilder>();
        
        HbaseIndexExportDesc indexExportDesc = HbaseDescCache.getIndexExportDescByName(table);
        if(indexExportDesc == null){
            return data;
        }
        
        for(Entry<String, Object> entry : data.entrySet()){
            boolean isMatch = false;
            for(Entry<String, Pattern> colPatternEntry : indexExportDesc.getCombinePatternMap().entrySet()){
                String col = colPatternEntry.getKey();
                Matcher matcher = colPatternEntry.getValue().matcher(entry.getKey());
                if(matcher.find()){
                    if(!combineResultMap.containsKey(col)){
                        combineResultMap.put(col, new StringBuilder());
                    }
                    if(!"0".equals(entry.getValue())){
                        String value = matcher.group(1);
                        combineResultMap.get(col).append(value).append(";");
                    }
                    isMatch = true;
                }
            }
            if(!isMatch){
                result.put(entry.getKey(), entry.getValue());
            }
        }
        
        for(Entry<String, StringBuilder> combineEntry : combineResultMap.entrySet()){
            StringBuilder builder = combineEntry.getValue();
            if(builder.length() != 0){
                builder.deleteCharAt(builder.length() - 1);
                result.put(combineEntry.getKey(), builder.toString());
            }
        }
        return result;
    }
    
}
