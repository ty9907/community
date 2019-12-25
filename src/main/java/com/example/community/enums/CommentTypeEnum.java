package com.example.community.enums;

public enum CommentTypeEnum {
    //type为1是问题的评论，type为2是评论的评论
    QUESTION(1),
    COMMENT(2);

    public Integer getType() {
        return type;
    }

    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for(CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()){
            if(commentTypeEnum.getType() == type)
                return true;
        }
        return false;
    }
}
