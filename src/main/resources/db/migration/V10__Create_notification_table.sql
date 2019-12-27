create table notification(
    id bigint auto_increment primary key ,  //通知id
    notifier bigint not null ,      //发出通知的人的id
    receiver bigint not null ,      //接收通知的人的id
    outerId bigint not null ,       //通知所属的id(问题id,评论id)
    type int not null ,             //通知类型(问题,评论)
    gmt_create bigint not null ,    //创建时间
    status int default 0 not NULL   //状态:已读,未读
)