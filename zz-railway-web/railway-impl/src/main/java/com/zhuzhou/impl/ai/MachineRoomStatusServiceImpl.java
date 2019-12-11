package com.zhuzhou.impl.ai;

import com.zhuzhou.entity.ai.MachineRoomStatus;
import com.zhuzhou.dao.ai.MachineRoomStatusMapper;
import com.zhuzhou.spi.ai.MachineRoomStatusService;
import com.zhuzhou.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenzeting
 * @since 2019-05-23
 */
@Service
public class MachineRoomStatusServiceImpl extends BaseServiceImpl<MachineRoomStatusMapper, MachineRoomStatus> implements MachineRoomStatusService {
    @Autowired
    private MachineRoomStatusMapper machineRoomStatusMapper;
}
