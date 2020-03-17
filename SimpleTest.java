import oqs.KEMs;

public class SimpleTest {
    
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
        
    }

}
