package org.semanticweb.sparql.owlbgpparser;

public class IOProperties {
    protected static IOProperties instance=new IOProperties();
    public static final int DEFAULT_CONNECTION_TIME_OUT=20000;
    public static final String CONNECTION_TIME_OUT_PROPERTY_NAME="owlapi.connectionTimeOut";
    public static final boolean DEFAULT_CONNECTION_ACCEPT_HTTP_COMPRESSION=true;
    public static final String CONNECTION_ACCEPT_HTTP_COMPRESSION_PROPERTY_NAME="owlapi.connectionAcceptHTTPCompression";
    
    protected int connectionTimeout;
    protected boolean connectionAcceptHTTPCompression=DEFAULT_CONNECTION_ACCEPT_HTTP_COMPRESSION;

    private IOProperties() {
        connectionTimeout=DEFAULT_CONNECTION_TIME_OUT;
        connectionAcceptHTTPCompression=DEFAULT_CONNECTION_ACCEPT_HTTP_COMPRESSION;
    }
    public static IOProperties getInstance() {
        return instance;
    }
    /**
     * Gets the connection timeout that is used for sockets when loading
     * ontologies over HTTP etc.
     * @return The connection timeout in milliseconds
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    /**
     * Sets the connection timeout that should be used when loading from
     * sockets.
     * @param connectionTimeout The connection timeout in milliseconds
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout=connectionTimeout;
    }
    /**
     * Determines whether HTTP compression can be used
     * @return <code>true</code> if HTTP compression can be used, otherwise
     * false
     */
    public boolean isConnectionAcceptHTTPCompression() {
        return connectionAcceptHTTPCompression;
    }
    /**
     * Sets whether HTTP compression can be used.
     * @param connectionAcceptHTTPCompression <code>true</code> if HTTP compression can
     * be used, otherwise <code>false</code>
     */
    public void setConnectionAcceptHTTPCompression(boolean connectionAcceptHTTPCompression) {
        this.connectionAcceptHTTPCompression=connectionAcceptHTTPCompression;
    }
}
