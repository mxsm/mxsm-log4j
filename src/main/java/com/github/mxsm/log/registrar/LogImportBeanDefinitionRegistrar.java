package com.github.mxsm.log.registrar;

import com.github.mxsm.log.EnableMxsmLog;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author mxsm
 * @date 2021/12/18 21:08
 * @Since 1.0.0
 */
public class LogImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * Register bean definitions as necessary based on the given annotation metadata of the importing {@code
     *
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry               current bean definition registry
     * @Configuration} class.
     * <p>Note that {@link BeanDefinitionRegistryPostProcessor } types may <em>not</em> be
     * registered here, due to lifecycle constraints related to {@code @Configuration} class processing.
     * <p>The default implementation is empty.
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
            importingClassMetadata.getAnnotationAttributes(EnableMxsmLog.class.getName(), false));
        boolean proxyTargetClass = attributes.getBoolean("proxyTargetClass");
        AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
        if (proxyTargetClass) {
            AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
        }

    }

}
