package io.vertx.ext.web.impl;

import com.jd.laf.web.vertx.Environment;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 环境感知的路由器
 */
public class MyRouter extends RouterImpl {

    protected static final Comparator<RouteImpl> routeComparator = (RouteImpl o1, RouteImpl o2) -> {
        // we keep a set of handlers ordered by its "order" property
        final int compare = Integer.compare(o1.order(), o2.order());
        // since we are defining the comparator to order the set we must be careful because the set
        // will use the comparator to compare the identify of the handlers and if they are the same order
        // are assumed to be the same comparator and therefore removed from the set.

        // if the 2 routes being compared by its order have the same order property value,
        // then do a more expensive equality check and if and only if the are the same we
        // do return 0, meaning same order and same identity.
        if (compare == 0) {
            if (o1.equals(o2)) {
                return 0;
            }
            // otherwise we return higher so if 2 routes have the same order the second one will be considered
            // higher so it is added after the first.
            return 1;
        }
        return compare;
    };

    protected final Set<RouteImpl> routes = new ConcurrentSkipListSet<>(routeComparator);
    protected final AtomicInteger orderSequence = new AtomicInteger();
    protected final Environment environment;
    protected Handler<Throwable> exceptionHandler;

    public MyRouter(final Vertx vertx, final Environment environment) {
        super(vertx);
        this.environment = environment;
    }

    @Override
    public void accept(final HttpServerRequest request) {
        //创建对环境感知的上下文
        new MyRoutingContext(null, this, request, routes, environment).next();
    }

    @Override
    public MyRoute route() {
        return new MyRoute(this, orderSequence.getAndIncrement());
    }

    @Override
    public MyRoute route(final HttpMethod method, final String path) {
        return new MyRoute(this, orderSequence.getAndIncrement(), method, path);
    }

    @Override
    public MyRoute route(final String path) {
        return new MyRoute(this, orderSequence.getAndIncrement(), path);
    }

    @Override
    public MyRoute routeWithRegex(final HttpMethod method, final String regex) {
        return new MyRoute(this, orderSequence.getAndIncrement(), method, regex, true);
    }

    @Override
    public MyRoute routeWithRegex(final String regex) {
        return new MyRoute(this, orderSequence.getAndIncrement(), regex, true);
    }

    @Override
    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

    @Override
    public Router clear() {
        routes.clear();
        return this;
    }

    @Override
    public void handleContext(final RoutingContext ctx) {
        new RoutingContextWrapper(getAndCheckRoutePath(ctx), ctx.request(), routes, ctx).next();
    }

    @Override
    public void handleFailure(final RoutingContext ctx) {
        new RoutingContextWrapper(getAndCheckRoutePath(ctx), ctx.request(), routes, ctx).next();
    }

    @Override
    protected Vertx vertx() {
        return super.vertx();
    }

    @Override
    public synchronized Router exceptionHandler(final Handler<Throwable> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    protected Handler<Throwable> exceptionHandler() {
        return exceptionHandler;
    }

    protected void add(RouteImpl route) {
        routes.add(route);
    }

    protected void remove(RouteImpl route) {
        routes.remove(route);
    }

    protected Iterator<RouteImpl> iterator() {
        return routes.iterator();
    }

    protected String getAndCheckRoutePath(final RoutingContext ctx) {
        Route currentRoute = ctx.currentRoute();
        String path = currentRoute.getPath();
        if (path == null) {
            throw new IllegalStateException("Sub routers must be mounted on constant paths (no regex or patterns)");
        }
        return path;
    }

}