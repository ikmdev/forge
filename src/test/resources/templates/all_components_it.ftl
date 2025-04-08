Here are all the variables:
    ${defaultSTAMPCalc}
    ${defaultLanguageCalc}
    ${defaultNavigationCalc}
    -----------------
Here are all the concept:
<#list concepts as concept>
    PublicId        -> ${concept.publicId}
    Nid             -> ${concept.nid}
    Desc            -> ${textOf(concept, defaultLanguageCalc)}
    <#list parentsOf(concept, defaultNavigationCalc) as parent>
    Parent          -> ${textOf(parent, defaultLanguageCalc)}
    Parent Nid     -> ${parent.publicId}
    </#list>
    <#list childrenOf(concept, defaultNavigationCalc) as child>
    Child           -> ${textOf(child, defaultLanguageCalc)}
    </#list>
    <#list ancestorsOf(concept, defaultNavigationCalc) as ancestor>
    Ancestor        -> ${textOf(ancestor, defaultLanguageCalc)}
    </#list>
    <#list descendentsOf(concept, defaultNavigationCalc) as descendant>
    Descendant      -> ${textOf(descendant, defaultLanguageCalc)}
    </#list>
    Entity Fast     -> ${concept}
    <#list concept.versions as conceptVersion>
    Concept Version STAMP Fast      -> ${entityGet(conceptVersion.stampNid)}
    </#list>
    <#assign singleConceptNid=concept.nid>
    -----------------
</#list>

<#list patterns as pattern>
    PublicId        -> ${pattern.publicId}
    Nid             -> ${pattern.nid}
    Desc            -> ${textOf(pattern, defaultLanguageCalc)}
    Pattern Fast    -> ${pattern}
    <#list pattern.versions as patternVersion>
    Pattern Version STAMP Fast      -> ${entityGet(patternVersion.stampNid)}
    </#list>
    -----------------
</#list>

Here are all the semantics:
<#list semantics as semantic>
    PublicId        -> ${semantic.publicId}
    Nid             -> ${semantic.nid}
    Semantic Fast   -> ${semantic}
    <#list semantic.versions as semanticVersion>
    Semantic Version STAMP Fast      -> ${entityGet(semanticVersion.stampNid)}
    </#list>
    -----------------
</#list>

Here are all the stamps:
    -----------------
<#list stamps as stamp>
    PublicId        -> ${stamp.publicId}
    Nid             -> ${stamp.nid}
    STAMP FAST      -> ${stamp}
    Values for stamp:
    Status          -> ${stamp.state}
    Time            -> ${stamp.time}
    Author          -> ${stamp.author}
    Module          -> ${stamp.module}
    Path            -> ${stamp.path}
    -----------------
</#list>