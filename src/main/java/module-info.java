import dev.ikm.tinkar.forge.wrapper.*;

module dev.ikm.tinkar.forge {
    requires dev.ikm.jpms.eclipse.collections.api;
    requires dev.ikm.tinkar.coordinate;
    requires dev.ikm.tinkar.entity;
    requires dev.ikm.tinkar.terms;
    requires freemarker;
    requires org.slf4j;

    exports dev.ikm.tinkar.forge;

    uses ForgeMethodWrapper;
    provides ForgeMethodWrapper with
            //Navigation Calculator Methods
            AncestorsOf,
            ChildrenOf,
            ParentsOf,
            DescendentsOf,
            //Language Calculator Methods
            TextOf,
            //Entity Service Component Lookup Methods
            GetConcept,
            GetEntity,
            GetPattern,
            GetSemantic,
            GetSTAMP,
            //Specific Semantic Lookup Methods
            DescriptionsFor;
}