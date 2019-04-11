package com.br.cobra.web.model;

import org.apache.commons.lang.StringUtils;

import com.br.cobra.web.constant.Constants;

public class HbaseIndexQueryDto {
    /**
     * 需要查询的表
     */
    private String table;
    /**
     * 查询条件
     */
    private String condition;
    /**
     * 结果列
     */
    private String column;
    /**
     * 请求offset
     */
    private String offset="";
    /**
     * limit限制
     */
    private int limit = Constants.PAGE_LIMIT;
    /**
     * 导出数据量
     */
    private String exportNum = "0";
    
    /**
     * 生成count计算总数
     * @return
     */
    public String getCountSql(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select count(*) from ");
        sqlBuilder.append(table);
        if(StringUtils.isNotBlank(condition)){
            sqlBuilder.append(" where ").append(condition);
        }
        
        return sqlBuilder.toString();
    }
    
    /**
     * 生成select查询语句
     * @return
     */
    public String getSelectSql(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select ");
        if(StringUtils.isNotBlank(column)){
            sqlBuilder.append(column);
        }else{
            sqlBuilder.append("cf");
        }
        
        sqlBuilder.append(" from ").append(table);
        if(StringUtils.isNotBlank(condition)){
            sqlBuilder.append(" where ").append(condition);
        }
        return sqlBuilder.toString();
    }
    
    /**
     * @return the {@link #table}
     */
    public String getTable() {
        return table;
    }
    /**
     * @param table
     * the {@link #table} to set
     */
    public void setTable(String table) {
        this.table = table;
    }
    /**
     * @return the {@link #condition}
     */
    public String getCondition() {
        return condition;
    }
    /**
     * @param condition
     * the {@link #condition} to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }
    /**
     * @return the {@link #column}
     */
    public String getColumn() {
        return column;
    }
    /**
     * @param column
     * the {@link #column} to set
     */
    public void setColumn(String column) {
        this.column = column;
    }
    /**
     * @return the {@link #offset}
     */
    public String getOffset() {
        return offset;
    }
    /**
     * @param offset
     * the {@link #offset} to set
     */
    public void setOffset(String offset) {
        this.offset = offset;
    }
    /**
     * @return the {@link #limit}
     */
    public int getLimit() {
        return limit;
    }
    /**
     * @param limit
     * the {@link #limit} to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * @return the {@link #exportNum}
     */
    public String getExportNum() {
        return exportNum;
    }

    /**
     * @param exportNum
     * the {@link #exportNum} to set
     */
    public void setExportNum(String exportNum) {
        this.exportNum = exportNum;
    }

}
