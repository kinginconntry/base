package com.needto.common.entity;

import com.needto.common.inter.Itree;
import com.needto.common.utils.Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 节点数据
 */
public class TreeData {

    public static final String SEP = ".";

    /**
     * 节点编码顺序集合
     */
    public List<String> codes;

    /**
     * 节点数据
     */
    public Itree tree;

    public TreeData(String codes, Itree tree) {
        if(!StringUtils.isEmpty(codes)){
            String[] codeList = codes.split("\\" + SEP);
            this.codes = new ArrayList<>(codeList.length);
            for(int i = 0, len = codeList.length; i < len; i++){
                if(!StringUtils.isEmpty(codeList[i])){
                    this.codes.add(codeList[i]);
                }
            }
        }else{
            this.codes = new ArrayList<>(0);
        }

        this.tree = tree;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Itree getTree() {
        return tree;
    }

    public void setTree(Itree tree) {
        this.tree = tree;
    }

    /**
     * 获取节点全名
     * @return
     */
    public String getTreeFullName(){
        if(CollectionUtils.isEmpty(this.codes)){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(String code : this.codes){
            stringBuilder.append(Utils.nullToString(code)).append(SEP);
        }
        if(stringBuilder.length() > 0){
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }else{
            return "";
        }
    }
}
