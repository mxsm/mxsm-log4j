package com.github.mxsm.log.selector;

import com.github.mxsm.log.EnableMxsmLog;
import com.github.mxsm.log.aop.LogConfig;
import com.github.mxsm.log.registrar.LogImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author mxsm
 * @date 2021/12/17 22:10
 * @Since 1.0.0
 */
public class LogImportSelector implements ImportSelector{

    /**
     * Select and return the names of which class(es) should be imported based on the {@link AnnotationMetadata} of the
     * importing @{@link Configuration} class.
     *
     * @param importingClassMetadata
     * @return the class names, or an empty array if none
     */
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
            importingClassMetadata.getAnnotationAttributes(EnableMxsmLog.class.getName(), false));
        boolean value = attributes.getBoolean("value");
        if (value) {
            return new String[]{LogConfig.class.getName(), LogImportBeanDefinitionRegistrar.class.getName()};
        }
        return new String[0];
    }
}
