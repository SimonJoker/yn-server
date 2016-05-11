package com.yunnong.mapper;

import com.yunnong.domain.Consultant;
import com.yunnong.domain.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Created by joker on 2016/4/27.
 */
@Transactional
@Repository(value = "orderMapper")
public interface OrderMapper {

    void create(Order order);

    List<HashMap<String, Object>> findOrderByStatus(Integer status);

    List<HashMap<String, Object>> findOrderOverTime(String date);

    List<HashMap<String, Object>> findOrderCancle();

    HashMap<String, Object> findOrderDetail(Long oid);
}
