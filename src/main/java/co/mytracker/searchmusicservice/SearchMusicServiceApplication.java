package co.mytracker.searchmusicservice;

import com.netflix.appinfo.AmazonInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableEurekaClient
public class SearchMusicServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchMusicServiceApplication.class, args);
	}

	//For being discoverable when deployed on AWS
	@Bean
	@Profile("!default")
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
		EurekaInstanceConfigBean bean = new EurekaInstanceConfigBean(inetUtils);
		AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
		bean.setDataCenterInfo(info);
		return bean;
	}

}
