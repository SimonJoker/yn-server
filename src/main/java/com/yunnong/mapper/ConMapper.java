package com.yunnong.mapper;

import com.yunnong.domain.Consultant;
import com.yunnong.domain.ConsultantOp;
import com.yunnong.domain.ConsultantTem;
import com.yunnong.domain.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joker on 2016/4/26.
 */
@Transactional
@Repository(value = "operMapper")
public interface ConMapper {

    void create(Consultant consultant);

    void modify(Consultant consultant);

    List<Consultant> findAll();

    List<ConsultantOp> findConAllOp();

    List<ConsultantTem> findNameImgDes();

    HashMap<String, Object> findConTimeAll(Map<String, Object> parm);

    Consultant findById(Long pid);

    HashMap<String, Object> findConTimeById(HashMap<String, Object> parm);

    Integer countAll();

}
