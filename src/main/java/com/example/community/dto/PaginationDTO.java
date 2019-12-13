package com.example.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;    //当前页的问题信息
    private boolean showPrevious;       //是否显示前一页按钮
    private boolean showfirstpage;      //是否显示首页按钮
    private boolean showEndPage;        //是否显示尾页按钮
    private boolean showNext;           //是否显示下一页按钮
    private Integer page;               //当前页码数
    private List<Integer> pages =new ArrayList<>();        //当前页显示的所有页码数（页码按钮）
    private Integer totalPage;          //总页数

    /**
     *
     * @param totalPage    总的页码数
     * @param page          当前页码
     */
    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage=totalPage;

        if(page<1)
            page=1;
        if(page> this.totalPage)
            page= this.totalPage;

        this.page=page;

        //添加当前页显示的所有页码按钮（在满足条件下，以当前页为中心最多前三页后三页）
        pages.add(page);    //首先添加当前页的按钮
        for(int i=1;i<=3;i++){
            //若当前页减一二三页大于零，则将页码数添加到显示按钮中，且添加到list头部，当前页之前
            if(page-i>0)
                pages.add(0,page-i);
            //若当前页加一二三页小于等于总页码数，则将页码数添加到显示按钮中，且添加到list尾部，当前页之后
            if(page+i<= this.totalPage)
                pages.add(page+i);
        }

        //如果当前页码数不为1，则显示上一页按钮，为1则不显示上一页按钮
        showPrevious = (page != 1);

        //如果当前页码数不为总的页码数，则显示下一页按钮，为总的页码数则不显示下一页按钮
        showNext= (page != this.totalPage);

        //如果显示的页码按钮不包含1则显示首页按钮，否则不显示首页按钮
        showfirstpage = !(pages.contains(1));

        //如果显示的页码按钮不包含尾页页码，则显示尾页按钮，否则不显示尾页按钮
        showEndPage= !(pages.contains(this.totalPage));
    }

}
