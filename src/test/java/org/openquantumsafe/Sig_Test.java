package org.openquantumsafe;
 
import org.openquantumsafe.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Sig_Test {
    
    public static void main(String[] args) {
        System.out.println("Supported Sigs:");
        Common.print_list(Sigs.get_supported_sigs());
        System.out.println();
        
        System.out.println("Enabled Sigs:");
        Common.print_list(Sigs.get_enabled_sigs());
        System.out.println();
        
    }

}
