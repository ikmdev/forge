<#list concepts as concept>
    PublicId        -> ${concept.publicId}
    Nid             -> ${concept}
    Desc            -> ${textOf(concept, defaultLanguageCalc)}
    <#list parentsOf(concept, defaultNavigationCalc) as parent>
        Parent          -> ${textOf(parent, defaultLanguageCalc)}
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
    Concept Entity Fast     -> ${entityGet(concept.nid)}
    <#list concept.versions as conceptVersion>
        Concept Version STAMP Fast      -> ${entityGet(conceptVersion.stampNid)}
    </#list>
    <#assign singleConceptNid=concept.nid>
    <#assign latestVersion = latestVersionOf(concept, defaultSTAMPCalc)>
    <#if latestVersion.isPresent() == true>
    Concept Latest Version ->  ${latestVersion.get()}
    </#if>
    -----------------
</#list>