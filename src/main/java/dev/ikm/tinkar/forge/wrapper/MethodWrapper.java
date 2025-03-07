package dev.ikm.tinkar.forge.wrapper;

import freemarker.template.TemplateMethodModelEx;

public interface MethodWrapper extends TemplateMethodModelEx {

    default String methodName() {
        String className = this.getClass().getSimpleName();
        char firstChar = className.charAt(0);
        return className.replace(firstChar, Character.toLowerCase(firstChar));
    }
}
