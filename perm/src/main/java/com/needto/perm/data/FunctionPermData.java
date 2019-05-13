package com.needto.perm.data;

import com.needto.perm.model.FunctionPerm;
import com.needto.perm.model.PermCat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class FunctionPermData {

    public PermCat permCat;

    public List<FunctionPerm> functionPermList;

    public FunctionPermData() {
        this.functionPermList = new ArrayList<>();
    }

    public PermCat getPermCat() {
        return permCat;
    }

    public void setPermCat(PermCat permCat) {
        this.permCat = permCat;
    }

    public List<FunctionPerm> getFunctionPermList() {
        return functionPermList;
    }

    public void setFunctionPermList(List<FunctionPerm> functionPermList) {
        this.functionPermList = functionPermList;
    }

    public void addFunctionPerm(FunctionPerm functionPerm){
        this.functionPermList.add(functionPerm);
    }
}
