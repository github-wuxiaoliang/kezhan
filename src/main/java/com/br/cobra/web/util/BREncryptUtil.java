package com.br.cobra.web.util;

import org.apache.commons.lang.StringUtils;

import com.bfd.enctype.Decode;
import com.bfd.enctype.Encode;
import com.br.cobra.common.util.MD5Utils;


/**
 * 
 * @author chun
 *
 */
public class BREncryptUtil {

    public static String encodeWithSalt(String key,int version,int type){
        if(StringUtils.isBlank(key)){
            return "";
        }
        String salt = MD5Utils.cell32(key).toUpperCase();
        if(key.length() < 32){
            salt = salt.substring(0,key.length()/2);
        }
        return salt+"@"+Encode.toEncode(version, type, salt+key);
    }
    
    public static String decodeWithSalt(String key){
        try{
            String salt = key.split("@")[0];
            if(salt.length() == key.length()){
                return "";
            }
            String decodeWithSalt = Decode.toDecodeSimple(key.substring(salt.length()+1));
            if(!decodeWithSalt.startsWith(salt)){
                return "";
            }else{
                return decodeWithSalt.substring(salt.length());
            }
        }catch(Throwable e){
            return "";
        }
    }
    
    /**
     * 解密key，最先使用加盐方式解密,解密失败,使用非加盐方式解密
     * @param key
     * @return
     */
    public static String decode(String key){
        String saltDecodeVal = decodeWithSalt(key);
        if(StringUtils.isNotBlank(saltDecodeVal)){
            return saltDecodeVal;
        }
        try{
            return Decode.toDecodeSimple(key);
        }catch(Exception e){
            return "";
        }
    }

}
