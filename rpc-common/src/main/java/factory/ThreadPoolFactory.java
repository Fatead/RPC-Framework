package factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建线程池的工具类
 */
public class ThreadPoolFactory {

    /**
     * 线程池参数
     */
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);

    private static Map<String, ExecutorService> threadPoolMap = new ConcurrentHashMap<>();

    private ThreadPoolFactory(){

    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix){
        return createDefaultThreadPool(threadNamePrefix,false);
    }

    /**
     * 根据默认参数创建线程池
     * @param threadNamePrefix
     * @param daemon
     * @return
     */
    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon){
        //computeIfAbsent： 如果key对应的value为空，则会将第二个参数的返回值存入
        ExecutorService pool = threadPoolMap.computeIfAbsent(threadNamePrefix, k -> createThreadPool(threadNamePrefix,daemon));
        //如果该线程已经属于关闭或者终止状态，则将该线程从threadPoolMap中移除并创建一个新的线程来替代
        if(pool.isShutdown() || pool.isTerminated()){
            threadPoolMap.remove(threadNamePrefix);
            pool = createThreadPool(threadNamePrefix, daemon);
            threadPoolMap.put(threadNamePrefix, pool);
        }
        return pool;
    }


    /**
     * 从ThreadPoolMap中取出所有的线程并关闭
     */
    public static void shutDownAll(){
        logger.info("关闭所有线程池");
        /*
         * parallelStream()属于Java 1.8 Stream API
         * parallelStream()是多线程对于容器进行操作，会容器分拆成几个部分，使用多线程并行操作
         */
        threadPoolMap.entrySet().parallelStream().forEach(entry ->{
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("关闭线程池 [{}] [{}]",entry.getKey(),executorService.isTerminated());
            try {
                executorService.awaitTermination(10,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("关闭线程池失败！");
                executorService.shutdownNow();
            }
        });
    }

    public static ExecutorService createThreadPool(String threadNamePrefix,boolean daemon){
        //创建默认大小的有界阻塞队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix,daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     * ThreadFactory主要是将一个线程的执行单元包装成一个线程对象，我们一般会指定这个线程的一些属性
     * 例如线程的名称、优先级、是否为守护线程
     * 在guava之中，我们可以使用ThreadFactoryBuilder来创建一个线程的工厂
     * @param threadNamePrefix 作为创建线程名字的前缀
     * @param daemon 指定是否为守护线程
     * @return
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon){
        if(threadNamePrefix!= null){
            if(daemon != null){
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
            }else {
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }

}
