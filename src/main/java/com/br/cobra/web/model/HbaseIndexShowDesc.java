package com.br.cobra.web.model;

/**
 * hbase索引展示信息描述
 * @author chun
 *
 */
public class HbaseIndexShowDesc {

    private String name;
    private String simpleName;
    private String[] head;
    private String[] col;
    private String[] defaultVal;
    
    public HbaseIndexShowDesc(String name, String simpleName, String[] head, String[] col,
            String[] defaultVal) {
        this.name = name;
        this.simpleName = simpleName;
        this.head = head;
        this.col = col;
        this.defaultVal = defaultVal;
    }
    /**
     * @return the {@link #name}
     */
    public String getName() {
        return name;
    }
    /**
     * @param name
     * the {@link #name} to set
     */
    public void setName(String name) {
        this.name = name;
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
}
