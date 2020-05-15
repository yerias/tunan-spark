public class JVMParams {

    public static void main(String[] args) throws InterruptedException {

//        System.out.println("hello,我是图南");

        Thread.sleep(1000);

        stack();

    }

    public static void stack(){
        stack();
    }
}
