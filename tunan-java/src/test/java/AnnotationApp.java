import com.tunan.java.annotation.TunanBean;
import com.tunan.java.annotation.TunanField;
import org.junit.Test;

import java.lang.reflect.Field;

public class AnnotationApp {

    @Test
    public void test01() throws Exception {
        Class<?> clazz = Class.forName("com.tunan.java.annotation.Animal");
        System.out.println(clazz.isAnnotationPresent(TunanBean.class));

        TunanBean tunanBean = clazz.getAnnotation(TunanBean.class);
        if (null != tunanBean) {
            System.out.println(tunanBean.table() + ":"
                    + tunanBean.from() + ":" +
                    tunanBean.annotationType()
            );
        }
    }

    @Test
    public void test02() throws Exception {
        Class<?> clazz = Class.forName("com.tunan.java.annotation.Animal");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields){
            TunanField tunanField = field.getAnnotation(TunanField.class);
            if(null != tunanField){
                System.out.println(field.getName()+" ==> "+tunanField.value());
            }
        }
    }

    @Test
    public void test03() throws Exception {
        Class<?> clazz = Class.forName("com.tunan.java.annotation.Pig");
        System.out.println(clazz.isAnnotationPresent(TunanBean.class));
    }
}
