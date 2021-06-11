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
        
        //Si ninguna de las 2 referencias vale 'null' se comprueba que apunten a la misma direccion de memoria.
        if(n1 == n2) {
        	return true;
        }

        //Se comprueba la igualda utilizando el contenido de cada objeto.
        return n1.equals(n2);
    }
}
