package com.br.cobra.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.br.cobra.web.model.ResponseDto;

@SuppressWarnings("unchecked")
public class BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将请求参数转化成map
     * @param req
     * @return
     */
    public Map<String, Object> convertRequestParamToMap(HttpServletRequest req){
        Map<String, String[]> param = req.getParameterMap();
        Map<String, Object> result = new HashMap<String, Object>();
        for(String key : param.keySet()){
            String[] values = param.get(key);
            if(values != null && values.length != 0 && StringUtils.isNotBlank(values[0])){
                result.put(key, values[0]);
            }
        }
        return result;
    }
    
    /**
     * 写错误信息
     * @param resp
     * @param responseDto
     */
    protected void writeErrorRepsonse(HttpServletResponse resp,ResponseDto<String> responseDto){
        
        resp.setHeader("Content-Type","application/json");
        resp.setCharacterEncoding("utf-8");  
        
        PrintWriter out = null;  
        try{  
            out = resp.getWriter();  
            out.write(JSON.toJSONString(responseDto));  
            out.flush();  
        }catch(IOException e){  
            e.printStackTrace();  
        }  
    }
    
    /**
     * 将数据导出到csv
     * @param resp
     * @param fileName
     * @param headerTitle
     * @param headerAttr
     * @param defaultValue
     * @param data
     * @throws UnsupportedEncodingException 
     */
    protected void exportDataToCsv(HttpServletResponse resp,String fileName,String[] headerTitle,
                                   String[] headerAttr,String[] defaultValue,List<Map<String, Object>> data) throws UnsupportedEncodingException {
        fileName = new String(fileName.getBytes(),"iso-8859-1");
        resp.setHeader("Content-Disposition", "attachment; filename="+fileName); 
        resp.setHeader("Content-Type","application/octet-stream; charset=GBK");
        Writer writer = null;
        try{
            writer = resp.getWriter();
            StringBuilder header = new StringBuilder();
            for(int i = 0;i < headerTitle.length;i++){
                header.append(headerTitle[i]);
                if(i != headerTitle.length -1){
                    header.append(",");
                }
            }
            writer.write(header.toString()+"\r\n");
            int pos = 0;
            for(Map<String, Object> jsonData : data){
                // 进行属性转换
                StringBuilder singleLine = new StringBuilder();
                for(int i = 0 ; i < headerAttr.length ; i++){
                    Object value = jsonData.get(headerAttr[i]);
                    if(value == null){
                        value = defaultValue[i];
                    }
                    singleLine.append(value.toString());
                    if(i != headerAttr.length -1){
                        singleLine.append("\t,");
                    }
                }
                writer.write(singleLine.toString()+"\r\n");
                pos ++;
                if(pos %100 == 0 ){
                    writer.flush();
                }
            }
        }catch(Exception e){
            LOGGER.error("导出csv失败."+e.getMessage(),e);
        }
        finally{
            if(writer != null){
                try{
                    // 关闭writer
                    writer.flush();
                    writer.close();
                }catch(Exception e){
                }
            }
        }
    }
    
}
