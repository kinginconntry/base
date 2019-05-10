package com.needto.common.utils;

import com.alibaba.fastjson.JSON;
import com.needto.common.inter.Itree;
import com.needto.common.entity.Tree;
import com.needto.common.entity.TreeData;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 树节点工具类
 */
public class TreeUtils {

    /**
     * 纵向节点转化为水平字典：结构
     * [
     *      {
     *          codes: ["1"],
     *          tree: {
     *              pcode: "1",
     *              code: "2"
     *          }
     *      },
     *      {
     *          codes: ["1"],
     *          tree: {
     *              pcode: "1",
     *              code: "2"
     *          }
     *      }
     * ]
     * @param trees
     * @return
     */
    public static <T extends Itree> List<TreeData> getHorizontal(List<T> trees){
        List<TreeData> res = new ArrayList<>();
        if(CollectionUtils.isEmpty(trees)){
            return res;
        }
        //所有节点字典
        Map<String, T> codeTrees = new HashMap<>(trees.size());
        Map<String, List<T>> pcodeTrees = new HashMap<>(trees.size());

        for(T tree : trees){
            codeTrees.put(tree.getCode(), tree);
        }
        // 顶级节点
        List<T> temp = new ArrayList<>();
        for(T tree : trees){
            String pcode = tree.getPcode();
            if(StringUtils.isEmpty(pcode) || !codeTrees.containsKey(pcode)){
                temp.add(tree);
            }
            if(!pcodeTrees.containsKey(pcode)){
                pcodeTrees.put(pcode, new ArrayList<>());
            }
            pcodeTrees.get(pcode).add(tree);
        }
        setChildrens("", temp, res, pcodeTrees);
        return res;
    }

    private static <T extends Itree> void setChildrens(String pcodes, List<T> childrens, final List<TreeData> res, final Map<String, List<T>> pcodeTreeDict){
        if(CollectionUtils.isEmpty(childrens)){
            return;
        }
        String pcodesSep;
        if(StringUtils.isEmpty(pcodes)){
            pcodesSep = "";
        }else{
            pcodesSep = pcodes + TreeData.SEP;
        }
        for(T tree : childrens){
            String pcodeTemp = pcodesSep + tree.getCode();
            res.add(new TreeData(pcodesSep, tree));
            childrens = pcodeTreeDict.get(tree.getCode());
            setChildrens(pcodeTemp, childrens, res, pcodeTreeDict);
        }
    }



    public static void main(String[] args){
        // 测试

        List<Itree> list = new ArrayList<>();
        list.add(new Tree<>("", "1"));
        list.add(new Tree("", "2"));
        list.add(new Tree("", "3"));
        list.add(new Tree("", "4"));

        list.add(new Tree("1", "5"));
        list.add(new Tree("5", "6"));

        list.add(new Tree("6", "10"));

        list.add(new Tree("2", "7"));

        list.add(new Tree("7", "8"));

        list.add(new Tree("d", "a"));
        list.add(new Tree("e", "b"));
        list.add(new Tree("f", "c"));

        list.add(new Tree("a", "g"));
        List<TreeData> res = getHorizontal(list);
        res.forEach((v) -> {
            System.out.println(JSON.toJSONString(v));
        });
//        System.out.println(getHorizontal(list));
    }
}
