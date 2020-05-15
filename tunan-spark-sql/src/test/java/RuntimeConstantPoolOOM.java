import java.util.HashSet;

public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        HashSet<String> set = new HashSet<String>();
        short i = 0;
        while(true){
            set.add(String.valueOf(i++).intern());
        }
    }
}
