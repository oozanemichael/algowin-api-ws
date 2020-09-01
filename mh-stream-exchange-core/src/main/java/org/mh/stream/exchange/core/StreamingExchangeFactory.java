package org.mh.stream.exchange.core;


import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.utils.Assert;
import org.knowm.xchange.exceptions.ExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Factory to provide the following to {@link StreamingExchange}:
 *
 * <ul>
 *   <li>Manages the creation of specific Exchange implementations using runtime dependencies
 * </ul>
 */
public enum StreamingExchangeFactory {

  INSTANCE;

  private static final Logger log = LoggerFactory.getLogger(StreamingExchangeFactory.class);

  /** Constructor */
  private StreamingExchangeFactory() {}

  /**
   * Create an Exchange object without default ExchangeSpecification
   *
   * <p>The factory is parameterised with the name of the exchange implementation class. This must
   * be a class extending {@link Exchange}.
   *
   * @param exchangeClassName the fully-qualified class name of the exchange
   * @return a new exchange instance configured with the default {@link
   *     ExchangeSpecification}
   */
  public StreamingExchange createExchangeWithoutSpecification(String exchangeClassName) {

    Assert.notNull(exchangeClassName, "exchangeClassName cannot be null");

    log.debug("Creating default exchange from class name");

    // Attempt to create an instance of the exchange provider
    try {

      // Attempt to locate the exchange provider on the classpath
      Class<?> exchangeProviderClass = Class.forName(exchangeClassName);

      // 测试类是否实现了Exchange
      if (Exchange.class.isAssignableFrom(exchangeProviderClass)) {
        // Instantiate through the default constructor and use the default exchange specification
        StreamingExchange exchange =
            (StreamingExchange) exchangeProviderClass.getConstructor().newInstance();
        return exchange;
      } else {
        throw new ExchangeException(
            "Class '" + exchangeClassName + "' does not implement Exchange");
      }
    } catch (ReflectiveOperationException e) {
      throw new ExchangeException("Problem creating Exchange ", e);
    }

    // Cannot be here due to exceptions

  }

  /**
   * Create an Exchange object with default ExchangeSpecification
   *
   * <p>The factory is parameterised with the name of the exchange implementation class. This must
   * be a class extending {@link org.knowm.xchange.Exchange}.
   *
   * @param exchangeClassName the fully-qualified class name of the exchange
   * @return a new exchange instance configured with the default {@link
   *     org.knowm.xchange.ExchangeSpecification}
   */
  public StreamingExchange createExchange(String exchangeClassName) {

    StreamingExchange exchange = getExchange(exchangeClassName);

    exchange.applySpecification(exchange.getDefaultExchangeSpecification());

    return exchange;
  }



  public StreamingExchange createExchange(String exchangeClassName, TradingArea area) {

    StreamingExchange exchange = getExchange(exchangeClassName);

    exchange.instance(area);

    exchange.applySpecification(exchange.getDefaultExchangeSpecification());

    return exchange;
  }


  public StreamingExchange getExchange(String exchangeClassName) {

    Assert.notNull(exchangeClassName, "exchangeClassName cannot be null");

    log.debug("Creating default exchange from class name");

    return createExchangeWithoutSpecification(exchangeClassName);
  }

  public StreamingExchange createExchange(ExchangeSpecification exchangeSpecification) {

    Assert.notNull(exchangeSpecification, "exchangeSpecfication cannot be null");

    log.debug("Creating exchange from specification");

    String exchangeClassName = exchangeSpecification.getExchangeClassName();

    // Attempt to create an instance of the exchange provider
    try {

      // Attempt to locate the exchange provider on the classpath
      Class<?> exchangeProviderClass = Class.forName(exchangeClassName);

      // Test that the class implements Exchange
      if (Exchange.class.isAssignableFrom(exchangeProviderClass)) {
        // Instantiate through the default constructor
        StreamingExchange exchange =
            (StreamingExchange) exchangeProviderClass.getConstructor().newInstance();
        exchange.applySpecification(exchangeSpecification);
        return exchange;
      } else {
        throw new ExchangeException(
            "Class '" + exchangeClassName + "' does not implement Exchange");
      }
    } catch (ReflectiveOperationException e) {
      throw new ExchangeException("Problem starting exchange provider ", e);
    }

    // Cannot be here due to exceptions

  }
}
