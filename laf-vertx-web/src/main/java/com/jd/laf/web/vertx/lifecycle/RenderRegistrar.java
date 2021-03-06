package com.jd.laf.web.vertx.lifecycle;

import com.jd.laf.web.vertx.Environment;
import com.jd.laf.web.vertx.EnvironmentAware;
import com.jd.laf.web.vertx.config.VertxConfig;
import io.vertx.core.Vertx;

import static com.jd.laf.web.vertx.Plugin.RENDER;

/**
 * 渲染处理器注册器
 */
public class RenderRegistrar implements Registrar {

    @Override
    public void register(final Vertx vertx, final Environment environment, final VertxConfig config) throws Exception {
        EnvironmentAware.setup(vertx, environment, RENDER.extensions());
    }

    @Override
    public int order() {
        //模板引擎是在handler里面进行了初始化
        return RENDER_ORDER;
    }
}
