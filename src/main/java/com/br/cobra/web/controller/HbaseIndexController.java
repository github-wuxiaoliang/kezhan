package com.br.cobra.web.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.br.cobra.common.util.PropertiesUtil;
import com.br.cobra.common.util.ValidateUtil;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.constant.HbaseDescCache;
import com.br.cobra.web.model.HbaseIndexExportDesc;
import com.br.cobra.web.model.HbaseIndexQueryDto;
import com.br.cobra.web.model.HbaseIndexShowDesc;
import com.br.cobra.web.model.PageMoreDataDto;
import com.br.cobra.web.model.ResponseDto;
import com.br.cobra.web.model.UserInfoDto;
import com.br.cobra.web.service.HbaseIndexService;
import com.br.cobra.web.util.BREncryptUtil;
import com.br.lightning_db.client.exception.SqlExecuteException;

/**
 * 群体用户画像hbase二级索引查询
 * @author chun
 *
 */
@Controller
@RequestMapping("/hbase_index")
public class HbaseIndexController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(HbaseIndexController.class);
    @Autowired
    private HbaseIndexService hbaseIndexService;
    private static final int MAX_EXPORT_DATA = NumberUtils.toInt(PropertiesUtil.getStringValue("export.max.data.count"),100000);
    private static final int DEFAULT_EXPORT_PAGE_SIZE = NumberUtils.toInt(PropertiesUtil.getStringValue("export.hbase.page.size"),10000);
    private static final String[] EXPORT_DECODE_USER_ARRAY = PropertiesUtil.getStringValue("export.decode.user").split(",");
    private static final int MULTI_QUERY_MAX_COUNT = NumberUtils.toInt(PropertiesUtil.getStringValue("select.hbase.multi.query.count"), 4000);
    private static final int DEFAULT_EXPORT_MAX_RANDOM_POS = NumberUtils.toInt(PropertiesUtil.getStringValue("export.hbase.max.random.pos"),100000);
    private static final int DEFAULT_LOGGER_TIME = 1000;

    /**
     * 进入hbase二级索引查询初始化界面
     * @param request
     * @return
     */
    @RequestMapping(value="list",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request){
        ModelAndView view = new ModelAndView("/hbaseIndex/list");
        view.getModel().put("ctx", request.getContextPath());
        
        List<HbaseIndexShowDesc> tableDescList = HbaseDescCache.getIndexShowDescList();
        view.getModel().put("tableDesc", tableDescList);
        view.getModel().put("tableDescJson", JSON.toJSONString(tableDescList).replace("\"", "\\\""));
        return view;
    }
    
    /**
     * 根据查询条件查询sql
     * @param request
     * @param param
     * @return
     */
    @RequestMapping(value="query",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseDto<PageMoreDataDto<Map<String, Object>>> query(HttpServletRequest request,HbaseIndexQueryDto param){
        long startTime = System.currentTimeMillis();
        PageMoreDataDto<Map<String, Object>> result = new PageMoreDataDto<Map<String, Object>>(new ArrayList<Map<String, Object>>(), false);

        // 参数检验
        if(StringUtils.isBlank(param.getTable())){
            return new ResponseDto<>(400, "请选择要查询的索引表名称.");
        }
        if(StringUtils.isBlank(param.getCondition())){
            result.addExtra("count", 0);
            return new ResponseDto<PageMoreDataDto<Map<String,Object>>>(result);
        }
        
        try{
            // 进行decode解码
            param.setCondition(URLDecoder.decode(param.getCondition(), "utf-8"));
            param.setOffset(URLDecoder.decode(param.getOffset(), "utf-8"));
            
            // 计算查询总数
            long count = hbaseIndexService.count(param);
            // 查询总数过少,增大查询limit,变为查询多个regionserver,提高查询速度
            if(count <= MULTI_QUERY_MAX_COUNT){
                param.setLimit(100);
            }
            // 获取查询详情信息
            result = hbaseIndexService.query(param);
            result.addExtra("count", count);
            
        }catch(SqlExecuteException e){
            return new ResponseDto<>(500, "查询hbase索引信息失败." + e.getMessage());
        }
        catch(Throwable e){
            LOGGER.error("查询hbase索引信息失败."+e.getMessage(), e );
            return new ResponseDto<>(500, "查询hbase索引信息失败.");
        }finally{
            long endTime = System.currentTimeMillis();
            if(endTime - startTime > DEFAULT_LOGGER_TIME){
                LOGGER.info("query cost time is "+(endTime - startTime));
            }
        }
        
        return new ResponseDto<PageMoreDataDto<Map<String,Object>>>(result);
    }

    /**
     * 根据数据rowkey查询详情
     * @param request
     * @param table
     * @param rowkey
     * @return
     */
    @RequestMapping(value="detail",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseDto<Map<String, Object>> detail(HttpServletRequest request, String table,String rowkey){
        
        try{
            Map<String, Object> detail = hbaseIndexService.detail(table, rowkey);
            return new ResponseDto<Map<String,Object>>(detail);
        }catch(Exception e){
            LOGGER.error("查询hbase详情信息失败."+e.getMessage(), e );
            return new ResponseDto<>(500, "查询hbase详情信息失败."+e.getMessage());
        }
    }
    
    /**
     * 导出满足条件数据
     * @param request
     * @param response
     * @param userIndexQueryDto
     */
    @RequestMapping(value="export",method={RequestMethod.GET,RequestMethod.POST})
    public void exportData(HttpServletRequest request, HttpServletResponse response ,HbaseIndexQueryDto param){
        List<Map<String, Object>> exportData = new ArrayList<Map<String,Object>>();
        long startTime = System.currentTimeMillis();
        UserInfoDto user = (UserInfoDto) request.getSession().getAttribute(Constants.SESSION_USER_INFO);
        
        int count = 0;
        int searchPage = 1;
        boolean isDecode = false;
        
    
        try{
            int exportNum = 0;
            exportNum = NumberUtils.toInt(param.getExportNum(), MAX_EXPORT_DATA);
            if(exportNum > MAX_EXPORT_DATA){
                exportNum = MAX_EXPORT_DATA;
            }
            
            HbaseIndexExportDesc exportDesc = HbaseDescCache.getIndexExportDescByName(param.getTable());
            
            if(exportDesc == null){
                LOGGER.error("不能找到对应"+param.getTable()+"导出配置信息.");
                writeErrorRepsonse(response, new ResponseDto<String>(400, "不能找到对应"+param.getTable()+"导出配置信息."));
                return;
            }
            // 判断是否需要解密数据
            for(String decodeUserId : EXPORT_DECODE_USER_ARRAY){
                if(decodeUserId.equals(""+user.getUserId())){
                    isDecode = true;
                    break;
                }
            }
            // 进行decode解码
            param.setCondition(URLDecoder.decode(param.getCondition(), "utf-8"));
                        
            int randomStartPos = 0;
            int currentPos = 0;
            
            if(exportNum == 0 || exportNum > MAX_EXPORT_DATA){
                exportNum = MAX_EXPORT_DATA;
            }
            
            // 计算随机查询起始查询位置
            if(exportNum != 0){
                long totalRecord =  hbaseIndexService.count(param);
                if(totalRecord > exportNum){
                    int randMaxPos = (int)(totalRecord  - exportNum);
                    if(randMaxPos > DEFAULT_EXPORT_MAX_RANDOM_POS){
                        randMaxPos = DEFAULT_EXPORT_MAX_RANDOM_POS;
                    }
                    
                    randomStartPos = RandomUtils.nextInt(randMaxPos);
                    searchPage = randomStartPos / DEFAULT_EXPORT_PAGE_SIZE+1;
                    currentPos = (searchPage - 1)*DEFAULT_EXPORT_PAGE_SIZE;
                }
            }
            
            param.setOffset("");
            param.setLimit(DEFAULT_EXPORT_PAGE_SIZE);
            
            // 生成要查询列名
            StringBuilder columnBuilder = new StringBuilder();
            if((exportDesc.getCombinePatternMap() == null || exportDesc.getCombinePatternMap().isEmpty()) 
                    && exportDesc.getCol() != null){
                String[] cols = exportDesc.getCol();
                for(int i = 0 ; i < cols.length; i++){
                    if("uniq_key".equals(cols[i])){
                        continue;
                    }
                    columnBuilder.append(exportDesc.getCol()[i]);
                    if( i != exportDesc.getCol().length -1){
                        columnBuilder.append(",");
                    }
                }
            }
            
            param.setColumn(columnBuilder.toString());
            do{
                
                PageMoreDataDto<Map<String, Object>> pageDto = hbaseIndexService.query(param);
                
                // 遍历结果,去除满足条件数据
                for(Map<String, Object> singleRecord : pageDto.getDetail()){
                    
                    // 解密keyId数据
                    if(isDecode){
                        for(String decodeCol : exportDesc.getDecodeCol()){
                        	if(singleRecord.get(decodeCol) != null 
                        			&& StringUtils.isNotBlank(singleRecord.get(decodeCol).toString())){
	                            // 修改解密方式为加盐解密
                        	    String decodeVal = BREncryptUtil.decode(singleRecord.get(decodeCol).toString());
	                            // 身份证不解密输出
	                            if(ValidateUtil.checkId(decodeVal)){
	                                singleRecord.put(decodeCol, singleRecord.get(decodeCol).toString());
	                            }else{
	                                singleRecord.put(decodeCol, decodeVal);
	                            }
                        	}
                        }
                    }
                    
                    if(currentPos >= randomStartPos){
                        exportData.add(singleRecord);
                        count++;
                    }
                    currentPos++;
                    if(count == exportNum){
                        break;
                    }
                }
                
                param.setOffset(pageDto.getExtra().get("offset").toString());
                searchPage++;
                // 判断数据是否已取完
                if(!pageDto.isHasMore()){
                    break;
                }
                
            }while(count < exportNum);
            String[] headTitle = exportDesc.getHead();
            String[] headAttr = exportDesc.getCol();
            String[] defaultValue = exportDesc.getDefaultVal();
            String fileName = param.getTable()+"_data_"+System.currentTimeMillis()+".csv";
            
            exportDataToCsv(response, fileName, headTitle, headAttr, defaultValue, exportData);
        }catch(SqlExecuteException e){
            writeErrorRepsonse(response, new ResponseDto<String>(500, "导出数据失败."+e.getMessage()));
        }
        catch(Exception e){
            LOGGER.error("导出数据失败."+e.getMessage(),e);
            writeErrorRepsonse(response, new ResponseDto<String>(500, "导出数据失败."));
        }finally{
            long endTime = System.currentTimeMillis();
            param.setOffset("");
            LOGGER.info("{} export {} ,cost time is {}",new Object[]{user.getUserName(),JSON.toJSONString(param), (endTime - startTime)});
        }
        
    }
}
