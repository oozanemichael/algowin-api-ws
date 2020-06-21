package org.mh.service.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;
import org.mh.service.netty.util.GZIPUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Log4j2
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private final StringBuilder currentMessage = new StringBuilder();

  public interface WebSocketMessageHandler {
    public void onMessage(String message);

    public void onMessage(byte[] message);
  }

  protected final WebSocketClientHandshaker handshaker;
  protected final WebSocketMessageHandler handler;
  private ChannelPromise handshakeFuture;

  public WebSocketClientHandler(
          WebSocketClientHandshaker handshaker, WebSocketMessageHandler handler) {
    this.handshaker = handshaker;
    this.handler = handler;
  }

  public ChannelFuture handshakeFuture() {
    return handshakeFuture;
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) {
    handshakeFuture = ctx.newPromise();
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    handshaker.handshake(ctx.channel());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    log.info("WebSocket Client disconnected! {}", ctx.channel());
  }

  @Override
  public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    Channel ch = ctx.channel();
    if (!handshaker.isHandshakeComplete()) {
      try {
        handshaker.finishHandshake(ch, (FullHttpResponse) msg);
        log.info("WebSocket Client connected! {}", ctx.channel());
        handshakeFuture.setSuccess();
      } catch (WebSocketHandshakeException e) {
        log.error("WebSocket Client failed to connect. {} {}", e.getMessage(), ctx.channel());
        handshakeFuture.setFailure(e);
      }
      return;
    }

    if (msg instanceof FullHttpResponse) {
      FullHttpResponse response = (FullHttpResponse) msg;
      throw new IllegalStateException(
          "Unexpected FullHttpResponse (getStatus="
              + response.status()
              + ", content="
              + response.content().toString(CharsetUtil.UTF_8)
              + ')');
    }

    WebSocketFrame frame = (WebSocketFrame) msg;
    if (frame instanceof TextWebSocketFrame) {
      dealWithTextFrame((TextWebSocketFrame) frame);
    } else if (frame instanceof ContinuationWebSocketFrame) {
      dealWithContinuation((ContinuationWebSocketFrame) frame);
    } else if (frame instanceof PingWebSocketFrame) {
      log.debug("WebSocket Client received ping");
      ch.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
    } else if (frame instanceof PongWebSocketFrame) {
      log.debug("WebSocket Client received pong");
    } else if (frame instanceof BinaryWebSocketFrame) {
      dealWithBinaryFrame((BinaryWebSocketFrame)frame);
    }else if (frame instanceof CloseWebSocketFrame) {
      log.info("WebSocket Client received closing");
      ch.close();
    }
  }

  private void dealWithBinaryFrame(BinaryWebSocketFrame frame) {
    ByteBuf content = frame.content();
    byte[] data = new byte[content.readableBytes()];
    content.readBytes(data);
    if (frame.isFinalFragment()) {
      handler.onMessage(data);
    }
    currentMessage.append(Arrays.toString(data));
  }

  private void dealWithTextFrame(TextWebSocketFrame frame) {
    if (frame.isFinalFragment()) {
      handler.onMessage(frame.text());
      return;
    }
    currentMessage.append(frame.text());
  }

  private void dealWithContinuation(ContinuationWebSocketFrame frame) {
    currentMessage.append(frame.text());
    if (frame.isFinalFragment()) {
      handler.onMessage(currentMessage.toString());
      currentMessage.setLength(0);
    }
  }

  /**
   * 异常捕获
   * */
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    log.error(
        "WebSocket client encountered exception ({} - {}). Closing",
        cause.getClass().getSimpleName(),
        cause.getMessage());
    if (!handshakeFuture.isDone()) {
      handshakeFuture.setFailure(cause);
    }
    ctx.close();
  }


}
