package com.example.processor;

import com.example.annotation.Print;
import com.google.auto.service.AutoService;

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
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Print.class);
        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile("PrintUtil");
            Writer writer = fileObject.openWriter();
            writer.write("package com.example.apt_demo;\n");
            writer.write("\n");
            writer.write("public class PrintUtil{\n");
            for (Element e: elements) {
                Name simpleName = e.getSimpleName();
                //processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,simpleName);
                writer.write("    //输出"+simpleName+"\n");
                writer.write("   public static void print$$"+simpleName+"() {\n");
                writer.write("        System.out.println(\"Hello "+simpleName+ "\");\n   }\n\n");

            }
            writer.write("}");
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
