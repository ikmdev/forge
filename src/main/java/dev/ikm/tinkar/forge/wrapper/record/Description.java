package dev.ikm.tinkar.forge.wrapper.record;

import dev.ikm.tinkar.terms.EntityProxy.Concept;

public record Description(Concept language, String text, Concept caseSignificance, Concept type) {
}
