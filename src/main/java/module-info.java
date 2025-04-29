module dev.ikm.tinkar.forge {
    requires dev.ikm.jpms.eclipse.collections.api;
    requires dev.ikm.tinkar.coordinate;
    requires dev.ikm.tinkar.entity;
    requires dev.ikm.tinkar.terms;
    requires freemarker;
    requires org.slf4j;

    exports dev.ikm.tinkar.forge;
    exports dev.ikm.tinkar.forge.wrapper.record;

    uses dev.ikm.tinkar.forge.ForgeMethodWrapper;
    provides dev.ikm.tinkar.forge.ForgeMethodWrapper with
            //Methods based on calculators
            dev.ikm.tinkar.forge.wrapper.of.AncestorsOf,
            dev.ikm.tinkar.forge.wrapper.of.ChildrenOf,
            dev.ikm.tinkar.forge.wrapper.of.ParentsOf,
            dev.ikm.tinkar.forge.wrapper.of.DescendentsOf,
            dev.ikm.tinkar.forge.wrapper.of.LatestVersionOf,
            dev.ikm.tinkar.forge.wrapper.of.TextOf,
            //Entity Service Component Lookup Methods
            dev.ikm.tinkar.forge.wrapper.get.EntityGet,
            //Specific Semantic Lookup Methods
            dev.ikm.tinkar.forge.wrapper.on.DescriptionsOn;
}