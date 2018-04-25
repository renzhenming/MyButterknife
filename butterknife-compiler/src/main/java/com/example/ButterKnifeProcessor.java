package com.example;

import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

import sun.rmi.runtime.Log;

/**
 * Created by renzhenming on 2018/4/24.
 * AbstractProcessor这个类是Java中的，只能在ava Library中使用
 */
@AutoService(Processor.class)
public class ButterKnifeProcessor extends AbstractProcessor {

    /**
     * init()方法会被注解处理工具调用，并输入ProcessingEnviroment参数。
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
     * @param processingEnv 提供给 processor 用来访问工具框架的环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    /**
     * 指定使用的Java版本，通常这里返回SourceVersion.latestSupported()，默认返回SourceVersion.RELEASE_6
     * @return  使用的Java版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 这里必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称
     * @return  注解器所支持的注解类型集合，如果没有这样的类型，则返回一个空集合
     */
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
    /**
     * 这相当于每个处理器的主函数main()，你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。
     * 输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素
     * @param set   请求处理的注解类型
     * @param roundEnvironment  有关当前和以前的信息环境
     * @return  如果返回 true，则这些注解已声明并且不要求后续 Processor 处理它们；
     *          如果返回 false，则这些注解未声明并且可能要求后续 Processor 处理它们
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");

        System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////");
        return false;
    }
}
