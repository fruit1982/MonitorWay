package cn.mw.monitor.screen.service.modelImpl;

import cn.mw.monitor.screen.service.WebSocket;
import cn.mw.monitor.screen.dto.ModelContentDto;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xhy
 * @date 2021/1/11 11:42
 * 20 日志量统计
 */
@Component
public class SecurityCountModel extends BaseModel {
    private static final Logger logger = LoggerFactory.getLogger("cn/mw/monitor/screen/SecurityCountModel");

    @Override
    public void process(ModelContentDto model) {
//        ThreadPoolExecutor executorService = new ThreadPoolExecutor(9, 20, 3, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
//        executorService.allowCoreThreadTimeOut(true);
        String modelDataId = model.getModelDataId();
        Integer userId = model.getUserId();

        int timeLag = dao.getBulkDataTimeCount(modelDataId, userId) == 0 ? 600 : dao.getBulkDataTime(modelDataId, userId);
        try {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    threadHashMap.put(userId+modelDataId,Thread.currentThread());
                    while (null != WebSocket.sessionPool.get(userId +modelDataId)) {
                        logger.info("{getSecurityCountByField_@timestamp}", modelDataId);
                        try {
                        String redisLeft = redisTemplate.opsForValue().get(genRedisKey("getSecurityCountByField_@timestamp_left", modelDataId, userId));
                        String redisRight = redisTemplate.opsForValue().get(genRedisKey("getSecurityCountByField_@timestamp_right", modelDataId, userId));
                        Map map = new HashMap();
                        if (null != redisLeft && StringUtil.isNotEmpty(redisLeft) && null != redisRight && StringUtil.isNotEmpty(redisRight)) {
                            Map<String, String> mapLeft = JSONArray.parseObject(redisLeft, Map.class);
                            Map<String, String> mapRight = JSONArray.parseObject(redisRight, Map.class);
                            Set<String> setLeft = mapLeft.keySet();
                            Set<String> setRight = mapRight.keySet();
                            for (String keyLeft : setLeft) {
                                map.put(keyLeft, mapLeft.get(keyLeft));
                            }
                            for (String keyRight : setRight) {
                                map.put(keyRight, mapRight.get(keyRight));
                            }
                        } else {
                            map = (Map) mwModelManage.getSecurityCountByField("@timestamp");
                        }
                        webSocket.sendObjMessage(userId,modelDataId, map);

                            Thread.sleep(1000 * model.getTimeLag());
                        } catch (InterruptedException e) {
                            logger.error("线程执行InterruptedException{}", e);
                            break;
                        }
                    }
                }
            };
            executorService.execute(runnable);
        } catch (Exception e) {
            logger.error("SCREEN_LOG[]screen[]大屏[]查询大屏的组件数据[]{}", e);
        }
    }
}
