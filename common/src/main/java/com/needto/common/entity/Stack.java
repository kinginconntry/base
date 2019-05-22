package com.needto.common.entity;

import java.util.ArrayList;
import java.util.List;

public class Stack<T> {
    private List<T> data;

    public Stack(){
        this.data = new ArrayList<>();
    }
    public Stack(int size){
        this.data = new ArrayList<>(size);
    }

    public int size(){
        return this.data.size();
    }

    public List<T> getData(){
        return this.data;
    }

    public void push(T o){
        this.data.add(o);
    }

    public T pop(){
        if(this.data.size() == 0){
            return null;
        }
        return this.data.remove(this.data.size() - 1);
    }

    public T top(){
        if(this.data.size() == 0){
            return null;
        }
        return this.data.get(this.data.size() - 1);
    }
}
