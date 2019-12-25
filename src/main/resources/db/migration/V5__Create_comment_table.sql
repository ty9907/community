create table comment(
    id bigint auto_increment primary key ,//评论id
    parent_id bigint not null ,            //父类id
    type int not null ,                    //评论类型，1为问题评论，2为评论的子评论
    commentator int not null ,             //评论者id
    gmt_create bigint not null ,
    gmt_modified bigint not null,
    like_count bigint default 0
)