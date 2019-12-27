package com.example.community.service;

import com.example.community.dto.NotificationDTO;
import com.example.community.dto.PaginationDTO;
import com.example.community.enums.NotificationStatusEnum;
import com.example.community.enums.NotificationTypeEnum;
import com.example.community.exception.CustomizeErrorCode;
import com.example.community.exception.CustomizeException;
import com.example.community.mapper.NotificationMapper;
import com.example.community.model.Notification;
import com.example.community.model.NotificationExample;
import com.example.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 查询通知信息
     * @param userId    用户id
     * @param page
     * @param size
     * @return
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalPage;

        //根据用户id查询用户总通知数
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1)
            page = 1;
        if (page > totalPage)
            page = totalPage;

        paginationDTO.setPagination(totalPage, page);

        //根据用户id分页查询用户的
        Integer offset = size * (page - 1);
        NotificationExample example=new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notifications=
                notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        if(notifications.size()==0){
            return paginationDTO;
        }

        //将查询到的通知信息封装，Notification通知类型为int，NotificationDTO通知类型为String，通过NotificationTypeEnum.nameOfType()转换
        List<NotificationDTO> notificationDTOS=new ArrayList<>();
        for(Notification notification:notifications){
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);

        return paginationDTO;
    }

    /**
     * 查询用户未查看的通知数量
     * @param userId    用户id
     * @return
     */
    public Long unReadCount(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    /**
     *读取通知操作
     * @param id
     * @param user
     * @return
     */
    public NotificationDTO read(Long id, User user) {
        Notification notification=notificationMapper.selectByPrimaryKey(id);
        if(notification==null)
            throw new CustomizeException((CustomizeErrorCode.NOTIFICATION_NOT_FOUND));
        if(!Objects.equals(notification.getReceiver(),user.getId()))
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);

        //设置该通知为已读状态
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

        return notificationDTO;
    }
}
