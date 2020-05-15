public class JavaVMStackOOM {

    private void dontStop(){
        while(true){
        }
    }

    public void stackleakByThread() {
        while(true){
            new Thread(() -> {
                    dontStop();
            }).start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackleakByThread();
    }
}
