package tunan.hadoop.flume.sources;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

public class MySource extends AbstractSource implements Configurable, PollableSource {

    // 自定义属性，前缀和后缀
    private String prefix;
    private String suffix;

    // 处理Event
    @Override
    public Status process() throws EventDeliveryException {
        // 自定义状态属性
        Status status = null;

        // 模拟产生数据
        for (int i = 0; i < 100; i++) {
            // 创建Event
            SimpleEvent event = new SimpleEvent();

            // 把数据设置到Body中去，注意header为空
            event.setBody((prefix + i + suffix).getBytes());
            // 开始处理Evetn
            getChannelProcessor().processEvent(event);
        }

        try {
            // 成功
            status = Status.READY;
        } catch (Exception e) {
            e.printStackTrace();
            // 失败，回退
            status = Status.BACKOFF;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 返回状态的结果
        return status;
    }


    // 获取Agent中传入的参数信息
    @Override
    public void configure(Context context) {
        this.prefix = context.getString("prefix","TUNAN");
        this.suffix = context.getString("suffix");
    }
}
