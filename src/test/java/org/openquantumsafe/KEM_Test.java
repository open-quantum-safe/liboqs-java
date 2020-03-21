package org.openquantumsafe;
 
import org.openquantumsafe.*;
import java.util.ArrayList;
import java.util.Arrays;

public class KEM_Test {
    
    public static <E, T extends Iterable<E>> void print_list(T list){
        for (Object element : list){
            System.out.print(element);
            System.out.print(" ");
        }
        System.out.println();
    }
    
    public static String shortHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        int num = 8;
        for (int i = 0; i < num; i++) {
            int v = bytes[i] & 0xFF;
            sb.append(HEX_ARRAY[v >>> 4]);
            sb.append(HEX_ARRAY[v & 0x0F]);
            sb.append(" ");
        }
        if (bytes.length > num*2) {
            sb.append("... ");
        }
        for (int i = bytes.length - num; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            sb.append(HEX_ARRAY[v >>> 4]);
            sb.append(HEX_ARRAY[v & 0x0F]);
            sb.append(" ");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        System.out.println("Supported KEMs:");
        print_list(KEMs.get_supported_KEMs());
        System.out.println();
        
        System.out.println("Enabled KEMs:");
        print_list(KEMs.get_enabled_KEMs());
        System.out.println();
        
        String kem_name = "DEFAULT";
        KeyEncapsulation client = new KeyEncapsulation(kem_name);
        client.print_details();
        System.out.println();

        long t = System.currentTimeMillis();
        byte[] client_public_key = client.generate_keypair();
        long timeElapsed = System.currentTimeMillis() - t;
        
        byte[] client_pk = client.export_public_key();
        byte[] client_sk = client.export_secret_key();
        
        System.out.println("Client public key:");
        System.out.println(shortHex(client_public_key));
        System.out.println("\nIt took " + timeElapsed + " millisecs to generate the key pair.");
        
        KeyEncapsulation server = new KeyEncapsulation(kem_name);

        t = System.currentTimeMillis();
        Pair<byte[], byte[]> server_pair = server.encap_secret(client_public_key);
        System.out.println("It took " + (System.currentTimeMillis() - t) + " millisecs to encapsulate the secret.");
        byte[] ciphertext = server_pair.getLeft();
        byte[] shared_secret_server = server_pair.getRight();
        
        t = System.currentTimeMillis();
        byte[] shared_secret_client = client.decap_secret(ciphertext);
        System.out.println("It took " + (System.currentTimeMillis() - t) + " millisecs to decapsulate the secret.");

        System.out.println("\nClient shared secret:");
        System.out.println(shortHex(shared_secret_client));
        System.out.println("\nServer shared secret:");
        System.out.println(shortHex(shared_secret_server));
        
        System.out.println("\nShared secrets coincide? " + Arrays.equals(shared_secret_client, shared_secret_server));
    }

}
