package com.microwu.cxd.network.netty.im.message;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:25
 * Update History:
 * Author        Time            Content
 */
public class LoginRequestMessage extends Message {

    private String username;
    private String password;

    public LoginRequestMessage() {

    }

    @Override
    public String toString() {
        return "LoginRequestMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
