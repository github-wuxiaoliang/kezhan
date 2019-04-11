package com.br.cobra.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.br.cobra.web.constant.HbaseDescCache;
import com.br.cobra.web.model.HbaseIndexExportDesc;

public class HbaseDescCacheTest {

    @Test
    public void testPattern(){
        HbaseIndexExportDesc exportDesc = HbaseDescCache.getIndexExportDescByName("Cobra_User_Data");
        
        Pattern pattern  = Pattern.compile("^cf\\|black_list_[^_]*$");
        Matcher matcher = pattern.matcher("cf|black_list_A1");
        System.out.println(matcher.find());

        pattern = Pattern.compile("^brand_[^_]*$");
        matcher =  pattern.matcher("brand_aacc");
        System.out.println(matcher.find());
        
        pattern = exportDesc.getCombinePatternMap().get("cf|black_list");
        matcher = pattern.matcher("cf|black_list_A1");
        System.out.println(matcher.find());
    }
}
