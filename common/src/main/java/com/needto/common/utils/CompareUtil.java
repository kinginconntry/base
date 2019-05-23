package com.needto.common.utils;

public class CompareUtil {

    public static int compareString(String a, String b){
        return compareString(a, b, true);
    }
    public static int compareString(String a, String b, boolean ase){
        int i = a.compareTo(b);
        if(ase){
            return i;
        }else{
            return -i;
        }
    }

    public static int compareInt(Integer a, Integer b){
        return compareInt(a, b, true);
    }

    public static int compareInt(Integer a, Integer b, boolean ase){
        int i = a.compareTo(b);
        if(ase){
            return i;
        }else{
            return -i;
        }
    }


    public static int compareLong(Long a, Long b){
        return compareLong(a, b, true);
    }

    public static int compareLong(Long a, Long b, boolean ase){
        int i = a.compareTo(b);
        if(ase){
            return i;
        }else{
            return -i;
        }
    }

    public static int compareDouble(Double a, Double b){
        return compareDouble(a, b, true);
    }

    public static int compareDouble(Double a, Double b, boolean ase){
        int i = a.compareTo(b);
        if(ase){
            return i;
        }else{
            return -i;
        }
    }
    public static int compareFloat(Float a, Float b){
        return compareFloat(a, b, true);
    }

    public static int compareFloat(Float a, Float b, boolean ase){
        int i = a.compareTo(b);
        if(ase){
            return i;
        }else{
            return -i;
        }
    }
}
