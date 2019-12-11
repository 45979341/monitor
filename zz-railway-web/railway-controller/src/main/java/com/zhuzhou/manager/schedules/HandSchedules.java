package com.zhuzhou.manager.schedules;

import com.zhuzhou.entity.link.Link;
import com.zhuzhou.entity.lkj.LkjIndex;
import com.zhuzhou.entity.video.IdxIndex;
import com.zhuzhou.entity.video.Lkj;
import com.zhuzhou.entity.video.Mp4;
import com.zhuzhou.phase.LkjHead;
import com.zhuzhou.spi.interior.InteriorService;
import com.zhuzhou.spi.link.LinkService;
import com.zhuzhou.spi.lkj.LkjIndexService;
import com.zhuzhou.spi.video.IdxIndexService;
import com.zhuzhou.spi.video.LkjService;
import com.zhuzhou.spi.video.Mp4Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


/**
 * @Author: chenzeting
 * Date:     2019/11/19
 * Description:  读取lkj,idx的api_status，代表分析项点完成，开始分析手势项点
 * 通过定时调度，分析手势项点。
 */
@Configuration
@Slf4j
@EnableScheduling
public class HandSchedules {

    @Autowired
    private LkjIndexService lkjIndexService;
    @Autowired
    private LinkService linkService;

    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * 30一次定时任务
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0/30 * * * * ?")
    public void configureTasks() {
        log.info("执行静态定时任务时间: {}" ,LocalDateTime.now());
        try{
            lock.lock();

            //查询出来需分析的id
            List<String> lkjs = lkjIndexService.getProcessIds();
            if(lkjs!=null && lkjs.size()>0){
                //获取已处理的id
                List<Link> links = linkService.list();
                if(links!=null && links.size()>0){
                    //取差集
                    List<String> collect = links.stream().map(Link::getLinkId).collect(Collectors.toList());
                    lkjs.removeAll(collect);
                }
                if(lkjs!=null && lkjs.size()>0){
                    //再进行收拾项点分析
                    Collection<LkjIndex> list = lkjIndexService.listByIds(lkjs);
                    analyse(lkjs,list.stream().collect(Collectors.toMap(LkjIndex::getId,e->e)));
                    log.info("分析手势项点结束: {}" ,LocalDateTime.now());
                    //入库
                    List<Link> linkList= new ArrayList<>();
                    for(String id:lkjs){
                        Link link = new Link();
                        link.setLinkId(id);
                        link.setStatus(1);
                        linkList.add(link);
                    }
                    linkService.saveBatch(linkList);
                    log.info("已入库完的id {}" ,lkjs);
                }else{
                    log.info("已分析完毕的");
                }
            }else{
                log.info("无需分析的id值");
            }
        }catch (Exception e){
            log.error("schedule is error");
        }finally {
            lock.unlock();
        }
    }

    @Autowired
    private InteriorService interiorService;
    @Autowired
    private LkjService lkjService;
    @Autowired
    private IdxIndexService idxIndexService;
    /**
     * 手势项点分析
     */
    private void analyse(List<String> list1, Map<String,LkjIndex> map){
        if(list1!=null && list1.size()>0){
            for(String id:list1){
                LkjIndex index = map.get(id);
                List<Lkj> list = lkjIndexService.remoteList(index.getFilePath());
                Mp4 mp4 = new Mp4();
                mp4.setRecordId(index.getId());
                LkjHead lkjHead = lkjService.processLkj(list);
                IdxIndex temp = new IdxIndex().setRecordId(id);
                IdxIndex idxIndex = idxIndexService.getOne(temp);
                interiorService.analysisAiPhase(lkjHead, id, list, idxIndex.getUrl());
            }
        }
    }
}
