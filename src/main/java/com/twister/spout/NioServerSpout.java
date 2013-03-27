package com.twister.spout;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.jboss.netty.handler.codec.frame.LineBasedFrameDecoder;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * is udp server
 * 
 * @author guoqing
 * 
 */
public class NioServerSpout {
	
	private ServerBootstrap bootstrap;
	private ChannelFactory channelFactory;
	private Channel serverChannel;
	private final int port;
	private final static int bufferSize = 1024;
	private volatile boolean running = false;
	
	public NioServerSpout(int port) {
		this.port = port;
	}
	
	public void run() {
		channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		bootstrap = new ServerBootstrap(channelFactory);
		try {
			// Set up the pipeline factory.
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline pipeline = Channels.pipeline();
					// Add the text line codec combination first,
					pipeline.addLast("framer", new LineBasedFrameDecoder(bufferSize));
					pipeline.addLast("decoder", new StringDecoder());
					pipeline.addLast("encoder", new StringEncoder());
					
					// and then business logic.
					pipeline.addLast("handler", new SpoutEventHandler(bufferSize));
					return pipeline;
				}
			});
			// bootstrap.setOption("reuseAddress", true);
			// bootstrap.setOption("tcpNoDelay", true);
			// bootstrap.setOption("broadcast", false);
			// bootstrap.setOption("sendBufferSize", bufferSize);
			// bootstrap.setOption("receiveBufferSize", bufferSize);
			// Bind and start to accept incoming connections.
			serverChannel = bootstrap.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
			running = true;
			System.out.println("server spout started, listening on port:" + port);
		} catch (UnknownHostException e) {
			stop();
		}
	}
	
	public void stop() {
		System.out.println("stopping UDP server");
		
		channelFactory.releaseExternalResources();
		bootstrap.releaseExternalResources();
		running = false;
		System.out.println("server stopped");
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 10237;
		}
		new NioServerSpout(port).run();
	}
	
}
