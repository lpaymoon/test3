/* *****************  JAVA头文件说明  ****************
 * file name  :  Server.java
 * owner      :  zhangwl
 * copyright  :  UMPAY
 * description:  
 * modified   :  Oct 14, 2011
 * *************************************************/ 

package com.umpay.hfrestbusi.core;

import java.util.Map.Entry;

import com.bs2.core.ext.Service4QRun;
import com.bs3.app.dal.engine.BeansServer;
import com.bs3.app.dal.engine.DalEngine2;
import com.bs3.ioc.core.BeansContext;
import com.bs3.utils.MyLog;
import com.bs3.utils.NamedProperties;
import com.umpay.hfrestbusi.constants.BusiConst;
import com.umpay.loadstrategy.configspy.ConfigSpier;


/* ******************  类说明  *********************
 * class       :  Server
 * @author     :  zhangwl
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/

public class Server {
	
	private static final MyLog _log = MyLog.getLog(Server.class);
	
	/* ********************************************
	 * method name   : main 
	 * description   : 
	 * @return       : void
	 * @param        : @param args
	 * modified      : zhangwl ,  Oct 14, 2011
	 * @see          : 
	 * ********************************************/

	public static void main(String[] args) {
		try {
			BeansContext ctx = BeansServer.main_start("beans.properties");	
			NamedProperties.register(BusiConst.SYSPARAMS, "system.properties");
			NamedProperties.register(BusiConst.MESSAGEPARAMS, "message.properties");
			NamedProperties.register(BusiConst.REQVALIDPARAMS, "reqvalid.properties");
			NamedProperties.register(BusiConst.PAYCHLPARAMS, "paychnl.properties");
			NamedProperties.refresh(false);
			//负载配置文件刷新线程启动 liujilong
			ConfigSpier cs = (ConfigSpier)BeansContext.getInstance().getBean("configSpier");
			cs.start();
			_log.info("负载配置文件刷新线程启动完成!");
			
			Service4QRun queue = (Service4QRun)BeansContext.getInstance().getBean("SmsSenderQueue");
			queue.putJob("综合支付资源服务启动");
			
			_log.info(ctx.getBeanActiveNames().toString());			
			for(Entry<String, String> entry: NamedProperties.getMap(DalEngine2.CFG_KEYNAME).entrySet()){
				_log.info(entry.getKey() + "=" + entry.getValue());//打印所有当前提供服务URL和实现类
				//System.out.println(entry.getKey() + "=" + entry.getValue());
			}
			_log.info("综合支付资源服务启动成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error(e, "综合支付资源服务启动失败");
		}		
	}

}



