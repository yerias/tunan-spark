package tunan.hadoop.flume.sink;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tunan
 */
public class MySink extends AbstractSink implements Configurable {

    // 拿到logger
    private static final Logger logger =  LoggerFactory.getLogger(MySink.class);

    // 属性作为参数的前缀和后缀
    private String prefix;
    private String suffix;


    // 从channel中获取数据发送到目的地
    @Override
    public Status process() throws EventDeliveryException {
        Status status;

        // 获取channel
        Channel ch = getChannel();
        // 获取事物
        Transaction txn = ch.getTransaction();
        // 开启事物
        txn.begin();
        try {
            // 拿到event
            Event event;

            do {   // 一直等待，直到拿到不为空的Event
                event = ch.take();
            } while ((event == null));

            // body是个字节数组转换成字符串
            String body = new String(event.getBody());

            // 控制台打印
            logger.error(prefix + body + suffix);

            // 提交事务
            txn.commit();
            status = Status.READY;
        } catch (Throwable t) {
            // 回滚事务
            txn.rollback();
            status = Status.BACKOFF;

            if (t instanceof Error) {
                throw (Error)t;
            }
        } finally {
            txn.close();
        }
        return status;
    }

    // 从Agent中拿到参数
    @Override
    public void configure(Context context) {
        this.prefix = context.getString("prefix","Tunan");
        this.suffix = context.getString("suffix");
    }
}
