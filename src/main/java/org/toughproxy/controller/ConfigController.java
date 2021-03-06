package org.toughproxy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.toughproxy.common.FileUtil;
import org.toughproxy.component.Memarylogger;
import org.toughproxy.config.Constant;
import org.toughproxy.form.ApiConfigForm;
import org.toughproxy.form.PoolConfigForm;
import org.toughproxy.form.SmsConfigForm;
import org.toughproxy.form.SystemConfigForm;
import org.toughproxy.common.RestResult;
import org.toughproxy.component.ConfigService;
import org.toughproxy.entity.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ConfigController implements Constant {

    @Autowired
    protected Memarylogger logger;

    @Autowired
    private ConfigService configService;

    @GetMapping(value = {"/api/config/load/{module}","/admin/config/load/{module}"})
    @ResponseBody
    public Map loadRadiusConfig(@PathVariable(name = "module") String module){
        Map result = new HashMap();
        try{
            List<Config> cfgs = configService.queryForList(module);
            for (Config cfg : cfgs){
                result.put(cfg.getName(),cfg.getValue());
            }
            if(module.equals("pool")){
                result.put("poolname",FileUtil.getFileContent(POOL_NAME_FILE).trim());
                result.put("dialupInterval",FileUtil.getFileContent(POOL_DIAUP_INTERVAL_FILE).trim());
                result.put("ipaddrType",FileUtil.getFileContent(POOL_IPADDR_TYPE_FILE).trim());
                result.put("areaCode",FileUtil.getFileContent(POOL_AREA_CODE_FILE).trim());
            }
        }catch(Exception e){
            logger.error("query config error",e, Memarylogger.SYSTEM);
        }
        return result;
    }

    /**
     * RADIUS 配置更新
     * @param form
     * @return
     */
    @PostMapping(value = {"/api/config/system/update","/admin/config/system/update"})
    @ResponseBody
    public RestResult updateSystemConfig(SystemConfigForm form){
        try{
            configService.updateConfig(new Config(SYSTEM_MODULE,SYSTEM_TICKET_HISTORY_DAYS,form.getSystemTicketHistoryDays()));
            configService.updateConfig(new Config(SYSTEM_MODULE,SYSTEM_SOCKS_USER_AUTH_MODE,form.getSystemSocksUserAuthMode()));
            configService.updateConfig(new Config(SYSTEM_MODULE,SYSTEM_SOCKS_RADIUS_AUTH_SERVER,form.getSystemSocksRadiusAuthServer()));
            configService.updateConfig(new Config(SYSTEM_MODULE,SYSTEM_SOCKS_RADIUS_AUTH_PORT,form.getSystemSocksRadiusAuthPort()));
            configService.updateConfig(new Config(SYSTEM_MODULE,SYSTEM_SOCKS_RADIUS_AUTH_SECRET,form.getSystemSocksRadiusAuthSecret()));
            configService.updateConfig(new Config(SYSTEM_MODULE,SYSTEM_SOCKS_RADIUS_NASID,form.getSystemSocksRadiusNasid()));
        }catch(Exception e){
            logger.error("update config error",e, Memarylogger.SYSTEM);
        }
        return new RestResult(0,"update system config done");
    }

    /**
     * RADIUS 配置更新
     * @param form
     * @return
     */
    @PostMapping(value = {"/api/config/pool/update","/admin/config/pool/update"})
    @ResponseBody
    public RestResult updatePoolConfig(PoolConfigForm form){
        try{
            FileUtil.writeFile(POOL_NAME_FILE,form.getPoolname().trim());
            FileUtil.writeFile(POOL_DIAUP_INTERVAL_FILE,form.getDialupInterval().trim());
            FileUtil.writeFile(POOL_IPADDR_TYPE_FILE,form.getIpaddrType().trim());
            FileUtil.writeFile(POOL_AREA_CODE_FILE,form.getAreaCode().trim());
        }catch(Exception e){
            logger.error("update config error",e, Memarylogger.SYSTEM);
        }
        return new RestResult(0,"update pool config done");
    }

    /**
     * 短信配置更新呢
     * @param form
     * @return
     */
    @PostMapping(value = {"/api/config/sms/update","/admin/config/sms/update"})
    @ResponseBody
    public RestResult updateSmsConfig(SmsConfigForm form){
        try{
            configService.updateConfig(new Config(SMS_MODULE,SMS_GATEWAY,form.getSmsGateway()));
            configService.updateConfig(new Config(SMS_MODULE,SMS_APPID,form.getSmsAppid()));
            configService.updateConfig(new Config(SMS_MODULE,SMS_APPKEY,form.getSmsAppkey()));
            configService.updateConfig(new Config(SMS_MODULE,SMS_VCODE_TEMPLATE,form.getSmsVcodeTemplate()));
        }catch(Exception e){
            logger.error("update config error",e, Memarylogger.SYSTEM);
        }
        return new RestResult(0,"update sms config done");
    }

    /**
     * API 配置更新呢
     * @param form
     * @return
     */
    @PostMapping(value = {"/admin/config/api/update"})
    @ResponseBody
    public RestResult updateApiConfig(ApiConfigForm form){
        try{
            configService.updateConfig(new Config(API_MODULE,API_TYPE,form.getApiType()));
            configService.updateConfig(new Config(API_MODULE,API_USERNAME,form.getApiUsername()));
            configService.updateConfig(new Config(API_MODULE,API_PASSWD,form.getApiPasswd()));
            configService.updateConfig(new Config(API_MODULE,API_ALLOW_IPLIST,form.getApiAllowIplist()));
            configService.updateConfig(new Config(API_MODULE,API_BLACK_IPLIST,form.getApiBlackIplist()));
        }catch(Exception e){
            logger.error("update config error",e, Memarylogger.SYSTEM);
        }
        return new RestResult(0,"update api config done");
    }



}

