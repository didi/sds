package com.didiglobal.sds.client.contant;

/**
 * Created by manzhizhen on 17/6/23.
 */
final public class BizConstant {

    /**
     * 每个桶的时间，单位秒
     */
    public static final int BUCKET_TIME = 1;

    /**
     * 每个（统计）周期内的桶数量
     * 注意，该值应该是10的整数倍并且能被60整除，这样比较合理，所以目前只能取值10、20、30
     * 并且：每个桶的时间固定，为1秒钟
     */
    public static final int CYCLE_BUCKET_NUM = 10;

    /**
     * 周期数量，为了上传统计数据方便，内存中保存上一个周期和当前周期的数据，并为下一个周期预留空间，所以是3个周期
     */
    public static final int CYCLE_NUM = 3;

    /**
     * 1秒内的毫秒数
     */
    public static final long MILLISECOND_IN_SECOND = 1000;

    /**
     * 应用组名称
     */
    public static final String APP_GROUP_NAME = "sds.app.group.name";

    /**
     * 应用名称
     */
    public static final String APP_NAME = "sds.app.name";

    /**
     * 服务端地址列表，如果是多个地址，那么用英文","隔开
     */
    public static final String SERVER_ADDR_LIST = "sds.server.addr.list";

}
