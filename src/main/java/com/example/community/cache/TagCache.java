package com.example.community.cache;


import com.example.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {

    /**
     * 设置预设标签
     * @return
     */
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS=new ArrayList<>();
        TagDTO program =new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("java","c","c++","c#","php","phython","javascript","html","css","golang"));
        tagDTOS.add(program);

        TagDTO framework =new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring","spring MVC","springBoot","mybatis","struts"));
        tagDTOS.add(framework);

        TagDTO server =new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("tomcat","linux","docker","unix","nginx","hadoop","apache"));
        tagDTOS.add(server);

        TagDTO db =new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis","mangodb","sql","oracle","sqlserver","postgresql","sqlite"));
        tagDTOS.add(db);

        TagDTO tool =new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","vim","eclipse","intellij-idea","svn","visual-studio"));
        tagDTOS.add(tool);
        return tagDTOS;
    }

    /**
     * 校检标签
     * @param tags  传入的标签
     * @return  返回非法的标签
     */
    public static String filterInvalid(String tags){
        String [] split = StringUtils.split(tags,",");
        List<TagDTO> tagDTOS= get();

        List<String> tagList =tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid =
                Arrays.stream(split).filter(t-> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;

    }
}
