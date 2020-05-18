package tunan.hadoop.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyInterceptor implements Interceptor {

    private  List<Event> newEvents;


    // 初始化设置
    @Override
    public void initialize()
    {
        newEvents = new ArrayList<>();
    }

    // 拦截单个Event
    @Override
    public Event intercept(Event event) {
        Map<String, String> headers = event.getHeaders();
        String body = new String(event.getBody());
        if (body.contains("gifshow")) {
            headers.put("type", "gifshow");
        } else {
            headers.put("type", "other");
        }

        return event;
    }

    // 拦截多个Event处理
    @Override
    public List<Event> intercept(List<Event> events) {
        // 每次进来初始化List
        newEvents.clear();
        Iterator<Event> iter = events.iterator();
        while (iter.hasNext()){
            Event next = iter.next();
            // event传递给单个处理，并添加到新的List
            newEvents.add(intercept(next));
        }

        // 返回拦截后的Event
        return newEvents;
    }

    @Override
    public void close() {

    }
    // 源码在 HostInterceptor，需要添加一个静态内部类，并且名为Builder
    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
