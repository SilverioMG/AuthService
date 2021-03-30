package net.atopecode.authservice.util;

public class SafeComparer {
    public static boolean equals(Object n1, Object n2){
        if((n1 == null) && (n2 == null)){
            return true;
        }
        if((n1 == null) && (n2 != null)){
            return false;
        }
        if((n1 != null) && (n2 == null)){
            return false;
        }

        return n1.equals(n2);
    }
}
