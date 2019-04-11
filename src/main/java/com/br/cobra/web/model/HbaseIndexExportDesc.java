package com.br.cobra.web.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 导出字段描叙
 * @author chun
 *
 */
public class HbaseIndexExportDesc {
    
    private String simpleName;
    private String[] head;
    private String[] col;
    private String[] defaultVal;
    private String[] decodeCol;
    private Map<String, Pattern> combinePatternMap ;
    
    public HbaseIndexExportDesc(){
        this.combinePatternMap = new HashMap<String, Pattern>();
    }
    
    public HbaseIndexExportDesc(String simpleName, String[] head, String[] col,
            String[] defaultVal, String[] decodeCol) {
        this();
        this.simpleName = simpleName;
        this.head = head;
        this.defaultVal = defaultVal;
        this.decodeCol = decodeCol;
        
        this.col = new String[col.length];
        
        for(int i = 0 ; i < col.length ; i++ ){
            String singleCol = col[i];
            if(singleCol.contains("_([^_]*)")){
                Pattern singleColPattern = Pattern.compile(singleCol);
                singleCol = singleCol.replace("_([^_]*)", "").replace("^", "").replace("$", "").replace("\\", "");
                this.combinePatternMap.put(singleCol, singleColPattern);
                this.col[i] = singleCol;
            }else{
                this.col[i] = singleCol;
            }
        }
        
    }

    /**
     * @return the {@link #simpleName}
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * @param simpleName
     * the {@link #simpleName} to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    /**
     * @return the {@link #head}
     */
    public String[] getHead() {
        return head;
    }

    /**
     * @param head
     * the {@link #head} to set
     */
    public void setHead(String[] head) {
        this.head = head;
    }

    /**
     * @return the {@link #col}
     */
    public String[] getCol() {
        return col;
    }

    /**
     * @param col
     * the {@link #col} to set
     */
    public void setCol(String[] col) {
        this.col = col;
    }

    /**
     * @return the {@link #defaultVal}
     */
    public String[] getDefaultVal() {
        return defaultVal;
    }

    /**
     * @param defaultVal
     * the {@link #defaultVal} to set
     */
    public void setDefaultVal(String[] defaultVal) {
        this.defaultVal = defaultVal;
    }

    /**
     * @return the {@link #decodeCol}
     */
    public String[] getDecodeCol() {
        return decodeCol;
    }

    /**
     * @param decodeCol
     * the {@link #decodeCol} to set
     */
    public void setDecodeCol(String[] decodeCol) {
        this.decodeCol = decodeCol;
    }

    /**
     * @return the {@link #combinePatternMap}
     */
    public Map<String, Pattern> getCombinePatternMap() {
        return combinePatternMap;
    }

    /**
     * @param combinePatternMap
     * the {@link #combinePatternMap} to set
     */
    public void setCombinePatternMap(Map<String, Pattern> combinePatternMap) {
        this.combinePatternMap = combinePatternMap;
    }
    
}
