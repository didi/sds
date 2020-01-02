package com.didiglobal.sds.client.listener;

import com.didiglobal.sds.client.enums.DowngradeActionType;

import java.util.Date;

/**
 * 降级动作监听器
 */
public interface DowngradeActionListener {

    /**
     * 触发降级事件
     *
     * @param point
     * @param downgradeActionType
     * @param time
     */
    void downgradeAction(String point, DowngradeActionType downgradeActionType, Date time);
}
