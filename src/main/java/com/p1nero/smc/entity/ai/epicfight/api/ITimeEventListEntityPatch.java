package com.p1nero.smc.entity.ai.epicfight.api;

import java.util.List;

/**
 * 用于添加时间戳事件
 */
public interface ITimeEventListEntityPatch {

    List<TimeStampedEvent> getTimeEventList();

    boolean addEvent(TimeStampedEvent event);
    default void clearEvents(){
        getTimeEventList().clear();
    };

}