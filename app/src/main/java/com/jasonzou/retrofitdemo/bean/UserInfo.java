package com.jasonzou.retrofitdemo.bean;

public class UserInfo {
    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean {
        public int status;
        public ListBean list;
        public int count;
        public int page;

        public static class ListBean {
            public int uid;
            public String account;
            public String realname;
            public String email;
            public int areaId;
            public String avatar;
            public String portrait;
            public String lawfirm;
            public String department;
            public String cardId;
            public int isAuth;
            public int isAccess;
            public String token;
            public String kariera;
            public String manager;
            public String technique;
            public int did;
            public int cdid;
            public int gdid;
            public int coefficient;
            public Object openId;
        }
    }
}
