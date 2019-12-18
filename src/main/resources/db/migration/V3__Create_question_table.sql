create table question
(
    id int auto_increment primary key,     --问题编号
    title varchar(50),      --问题标题
    description text ,
    gmt_create bigint,
    gmt_modified bigint,
    creator int,            --问题创建者
    comment_count int default 0,
    view_count int default 0,   --问题阅读数
    like_count int default 0,   --问题点赞数
    tag varchar(256)            --问题标签
);