package dev.ikm.tinkar.forge.wrapper;

import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractMethodWrapper implements TemplateMethodModelEx {

    private final Logger LOG = LoggerFactory.getLogger(AbstractMethodWrapper.class);


    public int extractNidFromParameters(List params) {
        if (params != null && params.size() != 1 && params.getFirst() != null && params.getFirst() instanceof SimpleNumber) {
            String errorMessage = "Improper Freemarker Parameter List for converting SimpleNumber to Integer Nid";
            LOG.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        return ((SimpleNumber) params.getFirst()).getAsNumber().intValue();
    }
}
