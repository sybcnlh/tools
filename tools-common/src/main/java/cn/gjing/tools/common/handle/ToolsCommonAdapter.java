package cn.gjing.tools.common.handle;

import cn.gjing.tools.common.util.id.IdUtils;
import cn.gjing.tools.common.util.id.SnowId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gjing
 **/
@Configuration
public class ToolsCommonAdapter {

    @Bean
    @ConditionalOnClass(NotEmptyProcessor.class)
    public NotEmptyProcessor notNullProxy() {
        return new NotEmptyProcessor();
    }
    @Bean
    @ConditionalOnMissingBean(SnowId.class)
    public SnowId snowId() {
        return new SnowId();
    }

    @Bean
    @ConditionalOnMissingBean(value = {IdUtils.class})
    @ConditionalOnBean(SnowId.class)
    public IdUtils idUtil() {
        return new IdUtils(snowId());
    }
}