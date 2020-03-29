package com.didiglobal.sds.client;

/**
 * 服务降级的客户端api
 * <p>
 * Created by manzhizhen on 17/1/1.
 */
public interface SdsClient {

    /**
     * 该请求是否需要降级
     *
     * @param point 降级点名称，每个应用内必须唯一
     * @return false-不需要降级，正常访问； true-需要降级
     */
    boolean shouldDowngrade(String point);

    /**
     * 用来标记业务处理时抛出的异常
     * 在需要使用异常率/异常量降级时需要用此方法来统计异常次数
     *
     * @param point 降级点名称
     * @param exception 业务处理时抛出的异常，如果不关心异常类型，该参数可以传null
     */
    void exceptionSign(String point, Throwable exception);

    /**
     * 退出降级逻辑
     * 该方法需要写到finally代码块中
     *
     * @param point 降级点名称
     */
    void downgradeFinally(String point);
}
