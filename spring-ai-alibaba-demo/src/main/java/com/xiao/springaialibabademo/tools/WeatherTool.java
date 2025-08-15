package com.xiao.springaialibabademo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.Map;

public class WeatherTool {

    @Tool(description = "获取指定地点天气")
    public String getWeatherByAddr(@ToolParam(description = "通过城市名称获取对应的天气，如北京，上海，深圳") String addr) {
        Map<String, String> addrMap =  Map.of("深圳", "雨, 18度", "北京", "晴, 30度", "上海", "阴, 20度");

        return addrMap.getOrDefault(addr, "未知地点, 无法获取天气信息");
    }

}
