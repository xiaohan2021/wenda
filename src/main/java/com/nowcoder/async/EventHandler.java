package com.nowcoder.async;

import java.util.List;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/11/4
 */
public interface EventHandler {
    void doHandler(EventModel model);

    List<EventType> getSupportEventTypes();
}
