package com.jasonzou.retrofitdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 项目:  LawyerOA <br>
 * 类名:  com.lcoce.lawyeroa.bean.IMConversationState<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/3 15:05<br>
 */
@Entity(generateConstructors = false
)
public class IMConversationState {
    @Id(autoincrement = true)
    private Long id;
    private boolean isTop;
    private boolean noDisturb;
    private long timeOfSetTop;
    @Unique()
    private String targetPhone;

    public IMConversationState() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean getNoDisturb() {
        return this.noDisturb;
    }

    public void setNoDisturb(boolean noDisturb) {
        this.noDisturb = noDisturb;
    }

    public long getTimeOfSetTop() {
        return this.timeOfSetTop;
    }

    public void setTimeOfSetTop(long timeOfSetTop) {
        this.timeOfSetTop = timeOfSetTop;
    }

    public String getTargetPhone() {
        return this.targetPhone;
    }

    public void setTargetPhone(String targetPhone) {
        this.targetPhone = targetPhone;
    }
}