package com.microwu.cxd.network.netty.demo;

public class NettyMessage {
	private Header header;
	private Object body;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
	public String toString(){
		return "NettyMessage [header=" + header + "]";
	}
}
