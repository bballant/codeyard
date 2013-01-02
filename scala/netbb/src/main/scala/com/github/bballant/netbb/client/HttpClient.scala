package com.github.bballant.netbb.client

import java.net.URI
import java.net.InetSocketAddress
import org.jboss.netty.bootstrap.ClientBootstrap
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory
import java.util.concurrent.Executors
import org.jboss.netty.channel.ChannelPipelineFactory
import org.jboss.netty.channel.ChannelPipeline
import org.jboss.netty.channel.Channels
import org.jboss.netty.handler.codec.http.HttpClientCodec
import org.jboss.netty.channel.SimpleChannelUpstreamHandler
import org.jboss.netty.channel.ChannelHandlerContext
import org.jboss.netty.channel.MessageEvent
import org.jboss.netty.handler.codec.http.HttpResponse
import scala.collection.JavaConverters._
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.util.CharsetUtil
import org.jboss.netty.channel.ChannelFuture
import org.jboss.netty.channel.Channel
import org.jboss.netty.handler.codec.http.DefaultHttpRequest
import org.jboss.netty.handler.codec.http.HttpVersion
import org.jboss.netty.handler.codec.http.HttpMethod
import org.jboss.netty.handler.codec.http.HttpRequest
import org.jboss.netty.handler.codec.http.HttpHeaders
import org.jboss.netty.handler.codec.http.CookieEncoder

object HttpClient {
  
  def main(args: Array[String]) {
    if (args.length != 1) {
      System.err.println("Usage: " + HttpClient.getClass.getSimpleName + " <URL>");
      return;
    }
    val uri: URI = new URI(args(0))
    get(uri)
  }
  
  def get(uri: URI) {
      
    val bootstrap: ClientBootstrap =
      new ClientBootstrap(
        new NioClientSocketChannelFactory(
          Executors.newCachedThreadPool,
          Executors.newCachedThreadPool))
    
    bootstrap.setPipelineFactory(new ClientPipelineFactory)
    
    val port = if (uri.getPort == -1) 80 else uri.getPort
    val future: ChannelFuture = bootstrap.connect(new InetSocketAddress(uri.getHost, port))
    val channel: Channel = future.awaitUninterruptibly.getChannel
    
    if (!future.isSuccess) {
        future.getCause().printStackTrace();
        bootstrap.releaseExternalResources();
        return;
    }
    
    val request: HttpRequest = new DefaultHttpRequest(
        HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath)
    
    request.setHeader(HttpHeaders.Names.HOST, uri.getHost)
    request.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE)
    request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP)

    // Set some example cookies.
    val httpCookieEncoder: CookieEncoder = new CookieEncoder(false)
    httpCookieEncoder.addCookie("my-cookie", "foo")
    httpCookieEncoder.addCookie("another-cookie", "bar")
    request.setHeader(HttpHeaders.Names.COOKIE, httpCookieEncoder.encode())

    // Send the HTTP request.
    channel.write(request)
    
    // Wait for the server to close the connection.
    channel.getCloseFuture().awaitUninterruptibly()
    
    // Shut down executor threads to exit.
    bootstrap.releaseExternalResources()
  }
}

class BBClientHandler extends SimpleChannelUpstreamHandler {
  override def messageReceived(ctx: ChannelHandlerContext, e: MessageEvent) {
    val response: HttpResponse = e.getMessage().asInstanceOf[HttpResponse]
    val content: ChannelBuffer = response.getContent()
    val lines = List (
      "--- HTTP /RESPONSE/ ---",
      "----- headers -->",
      response.getHeaders.asScala.map { headerEntry =>
        headerEntry.getKey + ": " + headerEntry.getValue
      }.mkString("\r\n"),
      "----- body -->",
      (if (content.readable) content.toString(CharsetUtil.UTF_8) else Nil),
      "--- END ---"
    )
        
    println(lines.mkString("\r\n"))
  }
}

class ClientPipelineFactory extends ChannelPipelineFactory {
  def getPipeline: ChannelPipeline = {
    val pipeline = Channels.pipeline
    pipeline.addLast("codec", new HttpClientCodec)
    pipeline.addLast("handler",  new BBClientHandler)
    pipeline
  }
}