package com.needto.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 概率生成器，主要用于从根据一堆概率数字随机获取出某个概率的位置
 */
public class ProbBuilder {

    public static class Section {
        /**
         * 开始位置
         */
        public long start;
        /**
         * 结束位置
         */
        public long end;
    }

    private long total = 0;

    private List<Long> bulks;

    private Map<String, Section> map;

    /**
     * 数值倍率
     */
    private int times;

    public ProbBuilder() {
        // 默认10000倍，保留4位小数
        this(10000);
    }

    public ProbBuilder(int times) {
        Assert.validateCondition(times <= 0);
        this.times = times;
        bulks = new ArrayList<>();
    }

    /**
     * 只有大于0才能进行概率计算，只保留4位小数
     * @param item
     */
    public void addItem(double item){
       if(item > 0){
           long temp = ((Double) (item * this.times)).longValue();
           bulks.add(temp);
           total += item;
           map = null;
       }
    }

    public void remove(int i){
        if(i >= 0 && i < bulks.size()){
            double res = bulks.remove(i);
            total -= res;
        }
    }

    public void clear(){
        this.map = null;
        this.total = 0;
        this.bulks  = new ArrayList<>();
    }

    private void prepare(){
        if(this.map == null){
            this.map = new HashMap<>();
            long totalTemp = 0;
            for(int i = 0, len = bulks.size(); i < len; i++){
                Section section = new Section();
                section.start = totalTemp;
                long temp = bulks.get(i);
                totalTemp += temp;
                section.end = totalTemp;
                this.map.put(i + "", section);
            }
        }
    }

    /**
     * 生成原理：丢塞子原理，落到哪个区间，哪个区间就被选中，区间的面积跟区间的值大小正向相关
     * 待优化，doubl在数据很大时，强转为long会丢失精度
     * 是否需要使用BigInteger
     *
     * @return
     */
    public int build(){
        if(bulks.size() == 0){
            return -1;
        }
        this.prepare();
        ;
        int res = 0;
        long prodVal = (long) (Math.random() * this.total);
        for(int i = 0, len = bulks.size(); i < len; i++){
            Section section = this.map.get(i+"");
            if(prodVal >= section.start && prodVal <= section.end){
                res = i;
                break;
            }
        }
        return res;
    }
}
