package ormapping;

import com.tunan.java.ormapping.domain.User;
import com.tunan.java.ormapping.utils.ReflectionUtils;
import org.junit.Test;

public class ReflectionUtilsApp {

    @Test
    public void getTable(){
        ReflectionUtils.getTable(User.class);
    }

    @Test
    public void getSQL (){
        ReflectionUtils.getSQL(User.class);
    }

}
