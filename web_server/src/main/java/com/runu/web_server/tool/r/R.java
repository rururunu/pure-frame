package com.runu.web_server.tool.r;

import java.util.HashMap;

/**
 * 统一返回值
 */
public class R extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;

    //状态码
    private int code;
    //返回内容
    private String msg;
    //数据对象
    private Object data;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 R 对象，使其表示一个空消息。
     * @param
     */
    public R() {
    }

    /**
     * 初始化一个新创建的 R 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public R(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        this.code=code;
        this.msg=msg;
    }

    /**
     * 初始化一个新创建的 R 对象
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public R(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        this.code=code;
        this.msg=msg;
        if (!(data==null)) {
            super.put(DATA_TAG, data);
            this.data=data;
        }
    }

    /**
     * 返回成功消息
     * @return 成功消息
     */
    public static R ok() {
        return R.ok("操作成功!");
    }

    /**
     * 返回成功消息
     * @param msg 返回内容
     * @return 成功消息
     */
    public static R ok(String msg) {
        return R.ok(msg, null);
    }

    /**
     * 返回成功数据
     * @return 成功消息
     */
    public static R ok(Object data) {
        return R.ok("操作成功!", data);
    }


    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static R ok(String msg, Object data) {
        return new R(200, msg, data);
    }

    /**
     * 返回错误消息
     */
    public static R error() {
        return R.error("操作失败!");
    }

    /**
     * 返回错误消息
     * @param msg 返回内容
     * @return 警告消息
     */
    public static R error(String msg) {
        return R.error(msg, null);
    }

    /**
     * 返回错误消息
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static R error(String msg, Object data) {
        return new R(500, msg, data);
    }

    /**
     * 返回错误消息
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static R error(int code, String msg) {
        return new R(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
