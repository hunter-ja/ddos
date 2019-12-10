package com.video.data;

import com.video.data.annotations.Command;
import com.video.data.command.BaseCommandI;
import com.video.data.command.model.ReturnMsg;
import com.video.data.model.ProxyIp;
import com.video.data.utils.SpringUtil;
import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class DdosApplication implements CommandLineRunner {

	private static Map<String, Class> methods;

	public static void main(String[] args) {
		SpringApplication.run(DdosApplication.class, args);
	}

	@Override
	public void run(String... args) {
		ReturnMsg returnMsg = new ReturnMsg();
		if (args.length < 1) {
			returnMsg.error("必须有参数");
			System.exit(0);
		}
		setMethods();
		Class method = methods.get(args[0]);
		if(method == null) {
			returnMsg.error("未找到可用方法");
			System.exit(0);
		}
		BaseCommandI command = (BaseCommandI) SpringUtil.getBean(method);
		command.run(args);
	}

	/**
	 * 扫描包找到所有能用的命令
	 */
	private void setMethods() {
		methods = new HashMap<>();
		Reflections reflections = new Reflections("com.video.data.command");
		Set<Class<? extends BaseCommandI>> classes = reflections.getSubTypesOf(BaseCommandI.class);
		for (Class clzz : classes) {
            Command command = (Command) clzz.getAnnotation(Command.class);
            if(command != null) {
                methods.put(command.value(), clzz);
            }
		}
	}

	@Bean
	public ProxyIp getProxyIp() {
		ProxyIp proxyIp = new ProxyIp();
		proxyIp.setProxyIps(new ArrayList<>());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		proxyIp.setStartTime(sf.format(new Date()));
		return proxyIp;
	}


}
