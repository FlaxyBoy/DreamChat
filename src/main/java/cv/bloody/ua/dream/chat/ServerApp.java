package cv.bloody.ua.dream.chat;

import cv.bloody.ua.dream.chat.handlers.MainHandler;
import cv.bloody.ua.dream.chat.handlers.PreLoginHandler;
import cv.bloody.ua.dream.chat.sql.ConnectionPool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class ServerApp
{

    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "";
    private static final String USER = "";
    private static final String PASSWORD = "";


    private static ChannelFuture future;
    public static final Logger logger = LogManager.getLogger(ServerApp.class);

    private static ConnectionPool pool;

    public static ConnectionPool getPool() {
        return pool;
    }

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            logger.info("Connect to data base");
            pool = new ConnectionPool(HOST, PORT,
                    DATABASE, USER, PASSWORD);
            logger.info("Conected");
            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                logger.info("Start server");
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new StringDecoder(), new StringEncoder(), new PreLoginHandler());
                            }

                        });
                future = bootstrap.bind(8888).sync();
                logger.info("Server started");
                future.channel().closeFuture().sync();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                pool.close();
            }
        });
        thread.setName("Server-Thread-1");
        thread.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if(scanner.nextLine().contains("/stop")) {
                try {
                    future.channel().close();
                    break;
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
