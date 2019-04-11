package com.br.cobra.web.model;

import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 登录用户基本信息
 * @author chun
 *
 */
public class UserInfoDto {
    private int roleId;
    private int userId;
    private String userName;
    private String roleName;
    private String areaName;
    private int areaId;
    
    public UserInfoDto(){
    }

    public UserInfoDto(JSONObject data){
        // {"status": "success", "zone_id": 16, "user_id": 41, "name": "\u90b9\u8fce\u6625", "zone": "\u65e0", "role": "\u65e0", "role_id": 14, "email": "yingchun.zou@100credit.com", "pro_id": 40}
        this.userName = data.getString("name");
        this.userId = data.getIntValue("pro_id");
        this.roleName = data.getString("role_name");
        this.roleId = data.getIntValue("role_id");
        if(roleId == 2 || roleId == 3){
            JSONObject zoneInfo = JSONObject.parseObject(data.getString("zone"));
            for(String key : zoneInfo.keySet()){
                this.areaId = NumberUtils.toInt(key);
                this.areaName = zoneInfo.getString(key);
            }
        }
    }
    
    /**
     * @return the {@link #roleId}
     */
    public int getRoleId() {
        return roleId;
    }
    /**
     * @param roleId
     * the {@link #roleId} to set
     */
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    /**
     * @return the {@link #userName}
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName
     * the {@link #userName} to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * @return the {@link #roleName}
     */
    public String getRoleName() {
        return roleName;
    }
    /**
     * @param roleName
     * the {@link #roleName} to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    /**
     * @return the {@link #areaName}
     */
    public String getAreaName() {
        return areaName;
    }
    /**
     * @param areaName
     * the {@link #areaName} to set
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    /**
     * @return the {@link #areaId}
     */
    public int getAreaId() {
        return areaId;
    }
    /**
     * @param areaId
     * the {@link #areaId} to set
     */
    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
    /**
     * @return the {@link #userId}
     */
    public int getUserId() {
        return userId;
    }
    /**
     * @param userId
     * the {@link #userId} to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
}
