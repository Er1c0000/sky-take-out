package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders order);

    Page<OrderVO> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getById(Long orderId);

    void update(Orders order);

    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    Page<Orders> pageQueryForAdmin(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
