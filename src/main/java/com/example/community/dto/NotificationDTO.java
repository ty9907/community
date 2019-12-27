package com.example.community.dto;

import com.example.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;                //通知id
    private Long gmtCreate;         //通知创建时间
    private Integer status;         //通知状态（已读未读）
    private Long notifier;          //发起通知的人的id
    private String notifierName;   //发起通知的人的名字
    private String outerTitle;      //通知标题
    private Long outerid;           //通知问题的id
    private String typeName;        //通知类型（问题、回复）名字
    private Integer type;           //通知类型（问题、回复)代码
}
