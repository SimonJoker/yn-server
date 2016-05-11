package com.yunnong.mapper;

import com.yunnong.domain.Consultant;
import com.yunnong.domain.OrderTime;
import com.yunnong.domain.OrderTimeTem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by joker on 2016/4/27.
 */
@Transactional
@Repository(value = "orderTimeMapper")
public interface OrderTimeMapper {

    void create(OrderTime orderTime);

    List<Long> findTidByPidDate(OrderTime orderTime);

    OrderTimeTem findByPidDate(OrderTime orderTime);

    List<OrderTime> findByPidDate();

    void modify(OrderTime orderTime);
}
