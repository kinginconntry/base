package com.needto.common.service;


import com.needto.common.entity.Stack;
import com.needto.common.utils.Assert;
import com.needto.common.utils.Utils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * 算式工具类
 */
public class EquationManager {

    private final static Pattern PATTERN = Pattern.compile("^[0-9]+.?[0-9]*$");


    private Map<String, IEquationDeal> map;

    public EquationManager(){
        map = new HashMap<>();
        IEquationDeal add = new IEquationDeal.Add();
        map.put(add.symbol(), add);

        IEquationDeal sub = new IEquationDeal.Sub();
        map.put(sub.symbol(), sub);

        IEquationDeal multi = new IEquationDeal.Multi();
        map.put(multi.symbol(), multi);

        IEquationDeal dev = new IEquationDeal.Devide();
        map.put(dev.symbol(), dev);
    }

    private static BigDecimal[] getParam(Stack<BigDecimal> stack){
        BigDecimal[] temp = new BigDecimal[stack.size()];
        int i = 0;
        while (stack.top() != null){
            temp[i] = stack.pop();
            i++;
        }
        return temp;
    }

    public void setEquation(IEquationDeal iEquationDeal){
        map.put(iEquationDeal.symbol(), iEquationDeal);
    }

    public IEquationDeal get(String key){
        return map.get(key);
    }

    private List<String> prepare(String exp){
        Assert.validateNull(exp);
        List<String> res = new ArrayList<>();
        if(StringUtils.isEmpty(exp)){
            return res;
        }
        List<String> symbolList = Utils.getList(exp, "");
        Stack<String> s = new Stack<>();
        for(String temp : symbolList){
            temp = Utils.trim(temp);
            if(StringUtils.isEmpty(temp)){
                continue;
            }
            if(map.get(temp) != null){
                while (s.size() != 0 && map.get(s.top()) != null && map.get(s.top()).priority() >= map.get(temp).priority()){
                    res.add(s.pop());
                }
                s.push(temp);
            }else if("(".equals(temp)){
                s.push(temp);
            }else if(")".equals(temp)){
                while (s.top() != null && !"(".equals(s.top())){
                    res.add(s.pop());
                }
                s.pop();
            }else if(",".equals(temp)){
                while (s.top() != null && !"(".equals(s.top())){
                    res.add(s.pop());
                }
            }else{
                res.add(temp);
            }
        }
        while (s.size() != 0){
            res.add(s.pop());
        }
        return res;
    }

    /**
     * 计算，外部可调用
     * @param exp
     * @return
     */
    public String cal(String exp){
        List<String> expList = prepare(exp);
        Stack<String> s = new Stack<>();
        for(String temp : expList){
            if(PATTERN.matcher(temp).matches()){
                s.push(temp);
            }else{
                Stack<BigDecimal> params = new Stack<>();
                int index = 0;
                IEquationDeal opt = map.get(temp);
                while (index < opt.getParamLen()){
                    params.push(BigDecimal.valueOf(Double.valueOf(s.pop())));
                    index++;
                }
                BigDecimal[] paramArr = getParam(params);
                Assert.validateCondition(paramArr.length < opt.getParamLen());
                BigDecimal res = opt.cal(paramArr);
                s.push(res.toString());
            }
        }
        return s.top();
    }

    public static void main(String[] args){
        String temp = "1*2+&(3, 1)*5/6";
        EquationManager equationManager = new EquationManager();
        equationManager.setEquation(new IEquationDeal() {
            @Override
            public String symbol() {
                return "&";
            }

            @Override
            public int priority() {
                return 1000;
            }

            @Override
            public BigDecimal cal(BigDecimal... params) {
                return params[0].add(params[1]);
            }
        });
        System.out.println(equationManager.cal(temp));
    }

}
