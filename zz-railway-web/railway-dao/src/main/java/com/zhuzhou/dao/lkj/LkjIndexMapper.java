package com.zhuzhou.dao.lkj;

import com.zhuzhou.entity.lkj.LkjIndex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-06
 */
@Repository
public interface LkjIndexMapper extends BaseMapper<LkjIndex> {

    @Select("select a.id from lkj_index a,idx_index b where a.id = b.record_id  and a.api_status = 1 and b.api_status = 1")
    List<String> getProcessIds();

}
