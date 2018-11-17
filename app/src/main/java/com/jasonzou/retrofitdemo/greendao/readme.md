#GreenDao 3.2.2 版本说明
 // greenDao
    implementation 'org.greenrobot:greendao:3.2.2'
    implementation 'org.greenrobot:greendao-generator:3.2.2'
    
####使用dao.save(),dao.insert(),id值均为自动增长，
    当前版本：如下定义
    
    public class User {
        //一定使用Long,(使用long会出现无法存入数据的问题)
        @Id
        private Long id;
        ...
        ...
        ...
    }
    
    
    
####使用dao.save()异常:Unsupported for entities with a non-null key，使用dao.insert()配合autoincrement
    public class User {
            //一定使用Long,(使用long会出现无法存入数据的问题)
            @Id(...)
            private long id;
            ...
            ...
            ...
    }
    
    
#主键
    @Id(autoincrement = true) private long id; 自动增长
    @Id private Long id; 自动增长

#数据类型 
    date => long|integer 14位(ms);
    boolean => 0|1;
   
        
    
    
    
