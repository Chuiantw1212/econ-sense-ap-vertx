package com.example.econ_sense_ap_vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();

    server.requestHandler(req -> {
      String memoryString = this.getMemoryUseInfo();
      req.response()
          .putHeader("content-type", "text/plain")
          .end(memoryString);
    });

    server.listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  public String getMemoryUseInfo() {
    MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    long getCommitted = mu.getCommitted();
    long getInit = mu.getInit();
    long getUsed = mu.getUsed();
    long max = mu.getMax();

    return ">>getCommitted(MB)=>" + getCommitted / 1000 / 1000 + "\n"
        + ">>getInit(MB)=" + getInit / 1000 / 1000 + "\n"
        + ">>getUsed(MB)=" + getUsed / 1000 / 1000 + "\n"
        + ">>max(MB)=" + max / 1000 / 1000 + "\n";
  }
}
