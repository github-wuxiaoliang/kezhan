package com.br.cobra.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.br.cobra.query.model.FieldCondition;
import com.br.cobra.web.constant.Constants;
import com.br.cobra.web.constant.HbaseDescCache;
import com.br.cobra.web.model.AccountChangeQueryDto;
import com.br.cobra.web.model.ApplyLoanQueryDto;
import com.br.cobra.web.model.HbaseIndexShowDesc;
import com.br.cobra.web.model.ResponseDto;

@Controller
@RequestMapping("/apply_loan")
public class ApplyLoanIndexController extends BaseController{
    
    @RequestMapping(value="list",method={RequestMethod.GET,RequestMethod.POST})
    public ModelAndView list(HttpServletRequest request,AccountChangeQueryDto accountChangeQueryDto,
                             @RequestParam(value="currentPage",defaultValue="1")int currentPage){
        ModelAndView view = new ModelAndView("/applyLoan/list");
        view.getModel().put("ctx", request.getContextPath());
        List<HbaseIndexShowDesc> tableDescList = HbaseDescCache.getIndexShowDescList();
        view.getModel().put("tableDesc", tableDescList);
        view.getModel().put("tableDescJson", JSON.toJSONString(tableDescList).replace("\"", "\\\""));

        return view;
    }
    
    @RequestMapping(value="generateQueryCondition",method={RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResponseDto<String> generateQueryCondition(ApplyLoanQueryDto applyLoanQueryDto){
        StringBuilder builder = new StringBuilder();
        try{
            List<FieldCondition> conditions = applyLoanQueryDto.generateFieldCondition();
            
            for( int i = 0; i < conditions.size(); i++ ){
                
                FieldCondition fieldCondition = conditions.get(i);
                if(i >= 1){
                    builder.append(" and ");
                }
                switch (fieldCondition.getConditionType()) {
                    case BETWEEN:
                        String[] values = fieldCondition.getValue().split("-");
                        builder.append(Constants.HBASE_DATA_COL).append(Constants.INDEX_FAMILY_COL_SEPATOR).append(fieldCondition.getFieldName()).append(" ").append(" between ");
                        builder.append(values[0]).append(" and ").append(values[1]);
                        break;
                    case EQUAL:
                        builder.append(Constants.HBASE_DATA_COL).append(Constants.INDEX_FAMILY_COL_SEPATOR).append(fieldCondition.getFieldName()).append("=").append(fieldCondition.getValue());
                    default:
                        break;
                }
            }
        }catch(Exception e){
            return new ResponseDto<>(400, "生成查询sql条件失败.");
        }
        
        return new ResponseDto<String>(builder.toString());
        
    }

}
