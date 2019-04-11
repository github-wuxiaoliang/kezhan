package com.br.cobra.web.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页加载更多数据
 * @author chun
 *
 */
public class PageMoreDataDto<T> {
    
    private List<T> detail;
    /**
     * 是否还有更多数据
     */
    private boolean hasMore;

    private Map<String, Object> extra;
    
    public PageMoreDataDto(List<T> detail, boolean hasMore) {
        this.detail = detail;
        this.hasMore = hasMore;
        this.extra = new HashMap<String, Object>();
    }
    
    /**
     * 添加额外扩展数据
     * @param key
     * @param val
     */
    public void addExtra(String key,Object val){
        this.extra.put(key, val);
    }
    
    /**
     * @return the {@link #detail}
     */
    public List<T> getDetail() {
        return detail;
    }

    /**
     * @param detail
     * the {@link #detail} to set
     */
    public void setDetail(List<T> detail) {
        this.detail = detail;
    }

    /**
     * @return the {@link #hasMore}
     */
    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * @param hasMore
     * the {@link #hasMore} to set
     */
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * @return the {@link #extra}
     */
    public Map<String, Object> getExtra() {
        return extra;
    }

    /**
     * @param extra
     * the {@link #extra} to set
     */
    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

}
