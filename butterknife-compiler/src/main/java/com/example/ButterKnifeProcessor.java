package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import javax.lang.model.util.Elements;

/**
 * Created by renzhenming on 2018/4/24.
 * AbstractProcessor这个类是Java中的，只能在ava Library中使用
 */
@AutoService(Processor.class)
public class ButterKnifeProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;

    /**
     * init()方法会被注解处理工具调用，并输入ProcessingEnviroment参数。
     * ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
     * @param processingEnv 提供给 processor 用来访问工具框架的环境
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
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
     *
     *          这里的log信息只能在gradle console中看到，Android logcat看不到。要注意
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //------------获取注解-----------

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        //LinkedHashMap输出和输入的顺序相同，先输入就先输出
        Map<Element,List<Element>> elementsMap = new LinkedHashMap<>();
        for (Element element : elements) {
            //这里会把所有跟注解有关的field全部拿到，包括各个类中的field,也就是说在
            //编译时，项目中所有涉及到这个注解的地方的所有field都在这个Set中返回了，
            //我们需要手动进行分类
            System.out.println("-----------------------"+element.getSimpleName());
            //得到的enclosingElement是这个field所在类的类名
            Element enclosingElement = element.getEnclosingElement();
            System.out.println("------------enclosingElement-----------"+enclosingElement.getSimpleName());
            //以类名位key值存储一个类中所有的field到集合中
            List<Element> bindViewElements = elementsMap.get(enclosingElement);
            if (bindViewElements == null){
                bindViewElements = new ArrayList<>();
                elementsMap.put(enclosingElement,bindViewElements);
            }
            bindViewElements.add(element);
        }

        //------------生成代码-----------

        for (Map.Entry<Element,List<Element>> entry:elementsMap.entrySet()){
            Element enclosingElement = entry.getKey();
            List<Element> bindViewElements = entry.getValue();

            ClassName unbinderClassName = ClassName.get("com.rzm.butterknife","Unbinder");
            System.out.println("------------Unbinder-----------"+unbinderClassName.simpleName());
            //得到类名的字符串
            String activityName = enclosingElement.getSimpleName().toString();
            ClassName activityClassName = ClassName.bestGuess(activityName);
            //拼装这一行代码：public final class xxx_ViewBinding implements Unbinder
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityName+"_ViewBinding")
                    //类名前添加public final
                    .addModifiers(Modifier.FINAL,Modifier.PUBLIC)
                    //添加类的实现接口
                    .addSuperinterface(unbinderClassName)
                    //添加一个成员变量，这个名字target是仿照butterknife
                    .addField(activityClassName,"target",Modifier.PRIVATE);

            //实现Unbinder的方法
            //CallSuper这个注解不像Override可以直接拿到，需要用这种方式
            ClassName callSuperClass = ClassName.get("android.support.annotation","CallSuper");
            MethodSpec.Builder unbindMethodBuilder = MethodSpec.methodBuilder("unbind")//和你创建的Unbinder中的方法名保持一致
                    .addAnnotation(Override.class)
                    .addAnnotation(callSuperClass)
                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);

            //添加构造函数
            MethodSpec.Builder constructMethodBuilder = MethodSpec.constructorBuilder()
                    .addParameter(activityClassName,"target");
            constructMethodBuilder.addStatement("this.target = target");
            for (Element bindViewElement : bindViewElements) {
                String fieldName = bindViewElement.getSimpleName().toString();

                //在构造方法中添加初始化代码
                ClassName utilsClassName = ClassName.get("com.rzm.butterknife", "Utils");
                BindView annotation = bindViewElement.getAnnotation(BindView.class);
                if (annotation != null){
                    int resId = annotation.value();
                    constructMethodBuilder.addStatement("target.$L = $T.findViewById(target,$L)",fieldName,utilsClassName,resId);

                    //在unbind方法中添加代码 target.textView1 = null;
                    //不能用addCode,因为它不会在每一行代码后加分号和换行
                    unbindMethodBuilder.addStatement("target.$L = null",fieldName);
                }

            }
            classBuilder.addMethod(constructMethodBuilder.build());


            classBuilder.addMethod(unbindMethodBuilder.build());

            //开始生成
            try {

                //得到包名
                String packageName = mElementUtils.getPackageOf(enclosingElement)
                        .getQualifiedName().toString();

                JavaFile.builder(packageName,classBuilder.build())
                        //添加类的注释
                        .addFileComment("butterknife 自动生成")
                        .build().writeTo(mFiler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
