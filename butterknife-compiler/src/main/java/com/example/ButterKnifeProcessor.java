package com.example;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * Created by renzhenming on 2018/4/24.
 * AbstractProcessor这个类是Java中的，只能在ava Library中使用
 */

public class ButterKnifeProcessor extends AbstractProcessor {

    //指定处理的版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    //需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //仿照Butternife源码
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation: getSupportAnnotations()){
             types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }
}
