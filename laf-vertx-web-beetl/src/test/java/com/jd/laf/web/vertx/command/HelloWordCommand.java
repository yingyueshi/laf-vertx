package com.jd.laf.web.vertx.command;

import com.jd.laf.web.vertx.Command;
import com.jd.laf.web.vertx.annotation.QueryParam;
import com.jd.laf.web.vertx.pool.Poolable;

public class HelloWordCommand implements Command, Poolable {

    @QueryParam("echo")
    String echo;

    @Override
    public Result execute() throws Exception {
        return new Result(echo == null ? "hello word!" : echo);
    }

    @Override
    public String type() {
        return "helloWorld";
    }

    @Override
    public void clean() {
        echo = null;
    }
}
