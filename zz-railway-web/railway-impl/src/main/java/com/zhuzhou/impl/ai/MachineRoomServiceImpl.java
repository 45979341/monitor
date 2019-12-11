package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.MachineRoom;
import com.zhuzhou.dao.ai.MachineRoomMapper;
import com.zhuzhou.spi.ai.MachineRoomService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-04-11
 */
@Service
public class MachineRoomServiceImpl extends BaseServiceImpl<MachineRoomMapper, MachineRoom> implements MachineRoomService {
    @Autowired
    private MachineRoomMapper machineRoomMapper;
}
