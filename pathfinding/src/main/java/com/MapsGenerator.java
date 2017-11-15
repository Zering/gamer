package com;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jps.Grids;
import com.map.DictMapDataVO;
import com.map.DictMapDefineVO;

/**
 * Created by zhanghaojie on 2017/11/10.
 * 用于生成地图基础信息,正式项目中一般是读取配置文件或数据库
 */
public class MapsGenerator {

    private static final Logger log = LoggerFactory.getLogger(MapsGenerator.class);

    private static final String path = "maps";

    private static Map<Short, DictMapDefineVO> mapCache = Maps.newHashMap();
    private static Map<Short, Grids> gridsCache = Maps.newHashMap();

    /**
     * 扫描指定包下的地图可走区域信息
     */
    public static void scanMaps() {
        try {
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(path);
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                if ("file".equals(url.getProtocol())) {
                    String packagePath = url.getPath().replaceAll("%20", " "); // 替换路径中的空格
                    File[] maps = new File(packagePath).listFiles(file -> file.isFile() && file.getName().endsWith(".json"));
                    if (maps == null) {
                        log.error("path[{}] not found any json file", packagePath);
                        continue;
                    }
                    for (File one : maps) {
                        short mapId = Short.parseShort(StringUtils.substringBefore(one.getName(), "."));
                        String fileStr = FileUtils.readFileToString(one, "UTF-8");

                        DictMapDataVO mapDataVO = JSON.parseObject(fileStr, DictMapDataVO.class);
                        Grids grids = new Grids(mapDataVO);
                        gridsCache.put(mapId, grids);

                        DictMapDefineVO mdDVO = new DictMapDefineVO();
                        mdDVO.setMapid(mapId);
                        mdDVO.setGridX((short) 10);
                        mdDVO.setGridY((short) 10);
                        mdDVO.setMapData(mapDataVO);
                        mapCache.put(mapId, mdDVO);
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<Short, DictMapDefineVO> getMapCache() {
        return mapCache;
    }

    public static DictMapDefineVO getMapInfo(short mapid) {
        return mapCache.get(mapid);
    }

    public static Map<Short, Grids> getGridsCache() {
        return gridsCache;
    }

    public static Grids getGridsInfo(short mapId) {
        return gridsCache.get(mapId);
    }
}
