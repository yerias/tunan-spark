package ormapping;

import com.tunan.java.ormapping.dao.impl.BaseDAOImpl;
import com.tunan.java.ormapping.domain.User;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date;

public class BaseDAOImplApp {

    @Test
    public void testSave(){
        BaseDAOImpl dao = new BaseDAOImpl();
        User user = new User("tunan", 18, new Date());
        Serializable id = dao.save(user);
        System.out.println(id);
    }

    @Test
    public void testTime(){
        System.out.println(new Date());
    }

}
