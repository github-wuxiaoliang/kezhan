package com.br.cobra.web.constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.web.model.HbaseIndexExportDesc;
import com.br.cobra.web.model.HbaseIndexShowDesc;
import com.br.lightning_db.client.hbase.HbaseClient;

/**
 * hbase 表描叙缓存
 * @author chun
 *
 */
public class HbaseDescCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbaseDescCache.class);

    private static List<HbaseIndexShowDesc> indexShowDescList;
    private static Map<String, HbaseIndexExportDesc> indexExportDescMap;
    
    
    static{
        init();
    }
    
    /**
     * 初始化表描叙
     */
    private static void init(){
        indexShowDescList = new ArrayList<HbaseIndexShowDesc>();
        indexExportDescMap = new HashMap<String, HbaseIndexExportDesc>();
        
        String tableNames = PropertiesUtil.getStringValue("hbase.all.table.name");
        String simpleTableNames = PropertiesUtil.getStringValue("hbase.all.table.simpleName");
        String[] tableNameArray = tableNames.split(",");
        String[] simpleTableNameArray = simpleTableNames.split(",");
        
        if(tableNameArray.length != simpleTableNameArray.length){
            LOGGER.error("hbase索引表的中文名称与英文名称长度不对应.");
        }
        
        for(int i = 0 ; i < simpleTableNameArray.length ; i++){
            String simpleName = simpleTableNameArray[i];
            String name = tableNameArray[i];
            
            // 初始化hbase表展示表头信息
            String head = PropertiesUtil.getStringValue("hbase.table."+simpleName+".show.head");
            String col = PropertiesUtil.getStringValue("hbase.table."+simpleName+".show.col");
            String defaultVal = PropertiesUtil.getStringValue("hbase.table."+simpleName+".show.default.val");
            if(StringUtils.isBlank(head) || StringUtils.isBlank(col) || StringUtils.isBlank(defaultVal)){
                LOGGER.error("不存在hbase索引表的"+simpleName+"展示列名配置信息.");
                continue;
            }
            String[] headArray = StringUtils.splitPreserveAllTokens(head, ",");
            String[] colArray = StringUtils.splitPreserveAllTokens(col, ",");
            String[] defaultValArray = StringUtils.splitPreserveAllTokens(defaultVal, ",");
            if(headArray.length != colArray.length || colArray.length != defaultValArray.length){
                LOGGER.error("hbase索引表的"+simpleName+"展示配置长度不一致.");
                continue;
            }

            // 初始化hbase导出表头信息
            String exportHead = PropertiesUtil.getStringValue("hbase.table."+simpleName+".export.head");
            String exportCol = PropertiesUtil.getStringValue("hbase.table."+simpleName+".export.col");
            String exportDefaultVal = PropertiesUtil.getStringValue("hbase.table."+simpleName+".export.default.val");
            if(StringUtils.isBlank(exportHead) || StringUtils.isBlank(exportCol) || StringUtils.isBlank(exportDefaultVal)){
                LOGGER.error("不存在hbase索引表的"+simpleName+"导出列名配置信息.");
                continue;
            }
            String[] exportHeadArray = StringUtils.splitPreserveAllTokens(exportHead, ",");
            String[] exportColArray = StringUtils.splitPreserveAllTokens(exportCol, ",");
            String[] exportDefaultValArray = StringUtils.splitPreserveAllTokens(exportDefaultVal, ",");
            if(exportHeadArray.length != exportColArray.length || exportColArray.length != exportDefaultValArray.length){
                LOGGER.error("hbase索引表的"+simpleName+"导出配置长度不一致.");
                continue;
            }
            String exportDecodeCol = PropertiesUtil.getStringValue("hbase.table."+simpleName+".export.decode.col");
            if(StringUtils.isBlank(exportDecodeCol)){
                exportDecodeCol = "";
            }
            String[] exportDecodeColArray = StringUtils.splitPreserveAllTokens(exportDecodeCol, ",");
            
            indexShowDescList.add(new HbaseIndexShowDesc(name, simpleName, headArray, colArray, defaultValArray));
            
            indexExportDescMap.put(simpleName, 
                new HbaseIndexExportDesc(simpleName, exportHeadArray, exportColArray, exportDefaultValArray, exportDecodeColArray));
            // 初始化hbase连接信息
            try {
                HbaseClient.getInstance().getHtable(simpleName);
            } catch (IOException e) {
                LOGGER.error("初始化hbase表连接失败"+e.getMessage());
            }
        }
        
    }
    
    /**
     * 根据表索引名称查询索引信息
     * @param simpleName
     * @return
     */
    public static HbaseIndexExportDesc getIndexExportDescByName(String simpleName){
        return indexExportDescMap.get(simpleName);
    }

    /**
     * @return the {@link #indexShowDescList}
     */
    public static List<HbaseIndexShowDesc> getIndexShowDescList() {
        return indexShowDescList;
    }

    /**
     * @param indexShowDescList
     * the {@link #indexShowDescList} to set
     */
    public static void setIndexShowDescList(List<HbaseIndexShowDesc> indexShowDescList) {
        HbaseDescCache.indexShowDescList = indexShowDescList;
    }

}
