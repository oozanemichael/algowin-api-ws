package org.mh.service.core;

import io.reactivex.Completable;

/** Base class of streaming services, declares connect() method including before connection logic */
public abstract class ConnectableService {

  /**
   * Exchange specific parameter is used for providing {@link Runnable} action which is caused
   * before setup new connection. For example adding throttle control for limiting too often opening
   * connections:
   *
   * <pre>{@code
   * static final TimedSemaphore limiter = new TimedSemaphore(1, MINUTES, 15);
   * ExchangeSpecification spec = exchange.getDefaultExchangeSpecification();
   * spec.setExchangeSpecificParameters(ImmutableMap.of(
   *   {@link org.mh.service.core.ConnectableService#BEFORE_CONNECTION_HANDLER}, () -> limiter.acquire()
   * ));
   * }</pre>
   */
  public static final String BEFORE_CONNECTION_HANDLER = "Before_Connection_Event_Handler";

  /** {@link Runnable} 连接之前需调用处理的程序. */
  private Runnable beforeConnectionHandler = () -> {};

  public void setBeforeConnectionHandler(Runnable beforeConnectionHandler) {
    if (beforeConnectionHandler != null) {
      this.beforeConnectionHandler = beforeConnectionHandler;
    }
  }

  /**
   * 抽象连接逻辑
   * */
  protected abstract Completable openConnection();

  public Completable connect() {
    beforeConnectionHandler.run();
    return openConnection();
  }
}
