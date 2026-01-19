/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package org.java_websocket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebSocket
extends WebSocketAdapter {
    private final Logger log = LoggerFactory.getLogger(AbstractWebSocket.class);
    private boolean tcpNoDelay;
    private boolean reuseAddr;
    private ScheduledExecutorService connectionLostCheckerService;
    private ScheduledFuture<?> connectionLostCheckerFuture;
    private long connectionLostTimeout = TimeUnit.SECONDS.toNanos(60L);
    private boolean websocketRunning = false;
    private boolean daemon = false;
    private final Object syncConnectionLost = new Object();
    private int receiveBufferSize = 0;
    protected static int DEFAULT_READ_BUFFER_SIZE = 65536;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getConnectionLostTimeout() {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            return (int)TimeUnit.NANOSECONDS.toSeconds(this.connectionLostTimeout);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setConnectionLostTimeout(int connectionLostTimeout) {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            this.connectionLostTimeout = TimeUnit.SECONDS.toNanos(connectionLostTimeout);
            if (this.connectionLostTimeout <= 0L) {
                this.log.trace("Connection lost timer stopped");
                this.cancelConnectionLostTimer();
                return;
            }
            if (this.websocketRunning) {
                this.log.trace("Connection lost timer restarted");
                try {
                    ArrayList<WebSocket> connections = new ArrayList<WebSocket>(this.getConnections());
                    for (WebSocket conn : connections) {
                        if (!(conn instanceof WebSocketImpl)) continue;
                        WebSocketImpl webSocketImpl = (WebSocketImpl)conn;
                        webSocketImpl.updateLastPong();
                    }
                }
                catch (Exception e2) {
                    this.log.error("Exception during connection lost restart", (Throwable)e2);
                }
                this.restartConnectionLostTimer();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void stopConnectionLostTimer() {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            if (this.connectionLostCheckerService != null || this.connectionLostCheckerFuture != null) {
                this.websocketRunning = false;
                this.log.trace("Connection lost timer stopped");
                this.cancelConnectionLostTimer();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void startConnectionLostTimer() {
        Object object = this.syncConnectionLost;
        synchronized (object) {
            if (this.connectionLostTimeout <= 0L) {
                this.log.trace("Connection lost timer deactivated");
                return;
            }
            this.log.trace("Connection lost timer started");
            this.websocketRunning = true;
            this.restartConnectionLostTimer();
        }
    }

    private void restartConnectionLostTimer() {
        this.cancelConnectionLostTimer();
        this.connectionLostCheckerService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("connectionLostChecker", this.daemon));
        Runnable connectionLostChecker = new Runnable(){
            private ArrayList<WebSocket> connections = new ArrayList();

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                this.connections.clear();
                try {
                    long minimumPongTime;
                    this.connections.addAll(AbstractWebSocket.this.getConnections());
                    Iterator<WebSocket> iterator2 = AbstractWebSocket.this.syncConnectionLost;
                    synchronized (iterator2) {
                        minimumPongTime = (long)((double)System.nanoTime() - (double)AbstractWebSocket.this.connectionLostTimeout * 1.5);
                    }
                    for (WebSocket conn : this.connections) {
                        AbstractWebSocket.this.executeConnectionLostDetection(conn, minimumPongTime);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                this.connections.clear();
            }
        };
        this.connectionLostCheckerFuture = this.connectionLostCheckerService.scheduleAtFixedRate(connectionLostChecker, this.connectionLostTimeout, this.connectionLostTimeout, TimeUnit.NANOSECONDS);
    }

    private void executeConnectionLostDetection(WebSocket webSocket, long minimumPongTime) {
        if (!(webSocket instanceof WebSocketImpl)) {
            return;
        }
        WebSocketImpl webSocketImpl = (WebSocketImpl)webSocket;
        if (webSocketImpl.getLastPong() < minimumPongTime) {
            this.log.trace("Closing connection due to no pong received: {}", (Object)webSocketImpl);
            webSocketImpl.closeConnection(1006, "The connection was closed because the other endpoint did not respond with a pong in time. For more information check: https://github.com/TooTallNate/Java-WebSocket/wiki/Lost-connection-detection");
        } else if (webSocketImpl.isOpen()) {
            webSocketImpl.sendPing();
        } else {
            this.log.trace("Trying to ping a non open connection: {}", (Object)webSocketImpl);
        }
    }

    protected abstract Collection<WebSocket> getConnections();

    private void cancelConnectionLostTimer() {
        if (this.connectionLostCheckerService != null) {
            this.connectionLostCheckerService.shutdownNow();
            this.connectionLostCheckerService = null;
        }
        if (this.connectionLostCheckerFuture != null) {
            this.connectionLostCheckerFuture.cancel(false);
            this.connectionLostCheckerFuture = null;
        }
    }

    public boolean isTcpNoDelay() {
        return this.tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public boolean isReuseAddr() {
        return this.reuseAddr;
    }

    public void setReuseAddr(boolean reuseAddr) {
        this.reuseAddr = reuseAddr;
    }

    public boolean isDaemon() {
        return this.daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public int getReceiveBufferSize() {
        return this.receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        if (receiveBufferSize < 0) {
            throw new IllegalArgumentException("buffer size < 0");
        }
        this.receiveBufferSize = receiveBufferSize;
    }
}

