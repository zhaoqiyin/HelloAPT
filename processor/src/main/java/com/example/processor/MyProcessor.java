package com.example.processor;

import com.example.annotation.Print;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
//@SupportedAnnotationTypes("com.example.apt_annotation.Print")
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MyProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.println("Hello1 APT");
        // 注解处理器提供的Api输出
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "Hello1 APT");
    }

    /**
     * 要扫描扫描的注解，可以添加多个
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(Print.class.getCanonicalName());
        return hashSet;
    }

    /**
     * 编译版本，固定写法就可以
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    /**
     * 扫描注解回调
     * process方法内部用到了直接拼接的方法，EventBus源码用的就是直接拼接的方法
     */
//    @Override
//    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
//        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Print.class);
//        try {
//            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile("PrintUtil");
//            Writer writer = fileObject.openWriter();
//            writer.write("package com.example.apt_demo;\n");
//            writer.write("\n");
//            writer.write("public class PrintUtil{\n");
//            for (Element e: elements) {
//                Name simpleName = e.getSimpleName();
//                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,simpleName);
//                writer.write("    //输出"+simpleName+"\n");
//                writer.write("   public static void print$$"+simpleName+"() {\n");
//                writer.write("        System.out.println(\"Hello "+simpleName+ "\");\n   }\n\n");
//
//            }
//            writer.write("}");
//            writer.flush();
//            writer.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }


    /**
     * 扫描注解回调
     * 以下用Javapoet生成
     * JavaPoet，这玩意好像可以帮我们以面向对象的思维来生成类，这样我们就不用手动拼接字符串的方式来生成类了
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //拿到所有添加Print注解的成员变量
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Print.class);

        //生成类
        TypeSpec.Builder classBuilder = TypeSpec
                .classBuilder("PrintUtil")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (Element element : elements) {
            //拿到成员变量名
            Name simpleName = element.getSimpleName();
            //生成方法
            MethodSpec method = MethodSpec.methodBuilder("print$$"+simpleName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                    .build();
            classBuilder.addMethod(method);
        }
        //包
        JavaFile javaFile = JavaFile
                .builder("com.example.apt_demo", classBuilder.build())
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }



}
