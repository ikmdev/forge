Here are all the variables:
    ${stringFieldConcept}
    ${textPattern}
    ${defaultSTAMPCalc}
    ${defaultLanguageCalc}
    ${defaultNavigationCalc}
    -----------------
Here are all the concept:
<#list concepts as concept>
    PublicId        -> ${concept.publicId}
    Nid             -> ${concept.nid}
    Desc            -> ${textOf(concept.nid, defaultLanguageCalc)}
    <#list parentsOf(concept.nid, defaultNavigationCalc) as parentNid>
    Parent          -> ${textOf(parentNid, defaultLanguageCalc)}
    </#list>
    <#list childrenOf(concept.nid, defaultNavigationCalc) as childNid>
    Child           -> ${textOf(childNid, defaultLanguageCalc)}
    </#list>
    <#list ancestorsOf(concept.nid, defaultNavigationCalc) as ancestorNid>
    Ancestor        -> ${textOf(ancestorNid, defaultLanguageCalc)}
    </#list>
    <#list descendentsOf(concept.nid, defaultNavigationCalc) as descendantNid>
    Descendant      -> ${textOf(descendantNid, defaultLanguageCalc)}
    </#list>
    Entity Fast     -> ${entityGet(concept.nid)}
    Concept Fast    -> ${conceptGet(concept.nid)}
    <#assign singleConceptNid=concept.nid>
    -----------------
</#list>

Here are all the patterns:
<#list patterns as pattern>
    PublicId        -> ${pattern.publicId}
    Nid             -> ${pattern.nid}
    Desc            -> ${textOf(pattern.nid, defaultLanguageCalc)}
    Pattern Fast    -> ${patternGet(pattern.nid)}
    <#list pattern.versions as patternVersion>
    STAMP Fast      -> ${stampGet(patternVersion.stampNid)}
    </#list>
    -----------------
</#list>

Here are all the semantics:
<#list semantics as semantic>
    PublicId        -> ${semantic.publicId}
    Nid             -> ${semantic.nid}
    Semantic Fast   -> ${semanticGet(semantic.nid)}
    -----------------
</#list>

Here is a Description Semantics for a concept using a Default Stamp Calculator:
    -----------------
    Default STAMP: ${defaultSTAMPCalc}

<#list descriptionsOn(singleConceptNid, defaultSTAMPCalc) as description>
    ${description}
</#list>
    -----------------

Here is a Description Semantic for a concept using a Primordial Stamp Calculator:
    Primordial STAMP: ${primordialSTAMPCalc}
<#list descriptionsOn(singleConceptNid, primordialSTAMPCalc) as description>
    ${description}
</#list>
    -----------------
