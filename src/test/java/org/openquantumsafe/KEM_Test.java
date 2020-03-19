package org.openquantumsafe;
 
import org.openquantumsafe.*;
import java.util.ArrayList;

public class KEM_Test {
    
    public static <E, T extends Iterable<E>> void print_list(T list){
        for (Object element : list){
            System.out.print(element);
            System.out.print(" ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        // test max_number_KEMs
        System.out.println("Num of KEMs " + KEMs.max_number_KEMs());
        System.out.println();
        
        // test is_KEM_enabled
        String alg = "foobar";
        System.out.println("is_KEM_enabled(\"" + alg + "\"): " + KEMs.is_KEM_enabled(alg));
        alg = "Kyber768";
        System.out.println("is_KEM_enabled(\"" + alg + "\"): " + KEMs.is_KEM_enabled(alg));
        System.out.println();
        
        // test get_KEM_name
        int alg_id = 0;
        System.out.println("get_KEM_name(" + alg_id + "): " + KEMs.get_KEM_name(alg_id));
        alg_id = 5;
        System.out.println("get_KEM_name(" + alg_id + "): " + KEMs.get_KEM_name(alg_id));
        System.out.println();
        
        // test is_KEM_supported
        alg = "foobar";
        System.out.println("is_KEM_supported(\"" + alg + "\"): " + KEMs.is_KEM_supported(alg));
        alg = "Kyber768";
        System.out.println("is_KEM_supported(\"" + alg + "\"): " + KEMs.is_KEM_supported(alg));
        System.out.println();
        
        // test get_supported_KEMs
        ArrayList<String> supported_KEMs = KEMs.get_supported_KEMs();
        print_list(supported_KEMs);
        System.out.println();
        
        // test get_enabled_KEMs
        ArrayList<String> enabled_KEMs = KEMs.get_enabled_KEMs();
        print_list(enabled_KEMs);
        System.out.println();
    
        // Create new KeyEncapsulation
        KeyEncapsulation ke = new KeyEncapsulation("Kyber768");
        ke.print_KeyEncapsulation();
        ke.print_details();
        
    }

}
