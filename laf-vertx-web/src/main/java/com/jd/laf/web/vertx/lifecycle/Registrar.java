package com.jd.laf.web.vertx.lifecycle;

import com.jd.laf.extension.Ordered;
import com.jd.laf.web.vertx.Environment;
import io.vertx.core.Vertx;

/**
 * 插件声明周期管理，进行初始化和销毁
 */
public interface Registrar extends Ordered {

    /**
     * 初始化
     *
     * @param vertx
     * @param environment 环境上下文
     * @throws Exception
     */
    void register(Vertx vertx, Environment environment) throws Exception;

    /**
     * 注销
     *
     * @param vertx vertx对象
     */
    default void deregister(Vertx vertx) {
    }

    /**
     * 优先级升序排序
     *
     * @return
     */
    default int order() {
        return ORDER;
    }


}
