package com.jasonzou.retrofitdemo.bean;

import java.util.List;

/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.bean.CaseList<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/17 15:21<br>
 */
public class CaseList {
    /**
     * msg : 请求成功
     * code : 1
     * data : {"status":1,"list":[{"id":1153,"title":"我叫你很帅","pid":2,"typeId":21,"coefficient":"1.00","caseStatus":0,"type":"常年法顾","name":"哈士狼","progress":"0.32","createTime":1509418113,"updateTime":1536924063,"expireTime":1512288540,"isCharge":1},{"id":1152,"title":"测试789","pid":1,"typeId":19,"coefficient":"1.00","caseStatus":0,"type":"其他纠纷","name":"","progress":"0.03","createTime":1509416579,"updateTime":1536806375,"expireTime":0,"isCharge":1},{"id":2753,"title":"呵呵","pid":1,"typeId":13,"coefficient":"1.00","caseStatus":0,"type":"婚姻继承","name":"呵呵","progress":"0.00","createTime":1530604380,"updateTime":1531991589,"expireTime":0,"isCharge":0},{"id":1141,"title":"g","pid":1,"typeId":19,"coefficient":"1.00","caseStatus":0,"type":"其他纠纷","name":"陈小成","progress":"0.00","createTime":1509264932,"updateTime":1530495684,"expireTime":0,"isCharge":1},{"id":323,"title":"顾问案件","pid":2,"typeId":21,"coefficient":"1.00","caseStatus":0,"type":"常年法顾","name":"张永","progress":"0.00","createTime":1491620742,"updateTime":0,"expireTime":1523116800,"isCharge":1}],"count":5,"page":2}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean {
        /**
         * status : 1
         * list : [{"id":1153,"title":"我叫你很帅","pid":2,"typeId":21,"coefficient":"1.00","caseStatus":0,"type":"常年法顾","name":"哈士狼","progress":"0.32","createTime":1509418113,"updateTime":1536924063,"expireTime":1512288540,"isCharge":1},{"id":1152,"title":"测试789","pid":1,"typeId":19,"coefficient":"1.00","caseStatus":0,"type":"其他纠纷","name":"","progress":"0.03","createTime":1509416579,"updateTime":1536806375,"expireTime":0,"isCharge":1},{"id":2753,"title":"呵呵","pid":1,"typeId":13,"coefficient":"1.00","caseStatus":0,"type":"婚姻继承","name":"呵呵","progress":"0.00","createTime":1530604380,"updateTime":1531991589,"expireTime":0,"isCharge":0},{"id":1141,"title":"g","pid":1,"typeId":19,"coefficient":"1.00","caseStatus":0,"type":"其他纠纷","name":"陈小成","progress":"0.00","createTime":1509264932,"updateTime":1530495684,"expireTime":0,"isCharge":1},{"id":323,"title":"顾问案件","pid":2,"typeId":21,"coefficient":"1.00","caseStatus":0,"type":"常年法顾","name":"张永","progress":"0.00","createTime":1491620742,"updateTime":0,"expireTime":1523116800,"isCharge":1}]
         * count : 5
         * page : 2
         */

        public int status;
        public int count;
        public int page;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 1153
             * title : 我叫你很帅
             * pid : 2
             * typeId : 21
             * coefficient : 1.00
             * caseStatus : 0
             * type : 常年法顾
             * name : 哈士狼
             * progress : 0.32
             * createTime : 1509418113
             * updateTime : 1536924063
             * expireTime : 1512288540
             * isCharge : 1
             */

            public int id;
            public String title;
            public int pid;
            public int typeId;
            public String coefficient;
            public int caseStatus;
            public String type;
            public String name;
            public String progress;
            public int createTime;
            public int updateTime;
            public int expireTime;
            public int isCharge;
        }
    }
}
