Here are all the concept:
<#list concepts as concept>
    PublicId        -> ${concept.publicId}
    Nid             -> ${concept.nid}
    Desc            -> ${description(concept.nid)}
    <#list parentsOf(concept.nid) as parentNid>
    Parent          -> ${description(parentNid)}
    </#list>
    <#list childrenOf(concept.nid) as childNid>
    Child           -> ${description(childNid)}
    </#list>
    <#list ancestorsOf(concept.nid) as ancestorNid>
    Ancestor        -> ${description(ancestorNid)}
    </#list>
    <#list descendantsOf(concept.nid) as descendantNid>
    Descendant      -> ${description(descendantNid)}
    </#list>
    Entity Fast     -> ${entityGetFast(concept.nid)}
    Concept Fast    -> ${conceptGetFast(concept.nid)}
    -----------------
</#list>

Here are all the patterns:
<#list patterns as pattern>
    PublicId        -> ${pattern.publicId}
    Nid             -> ${pattern.nid}
    Desc            -> ${description(pattern.nid)}
    Pattern Fast    -> ${patternGetFast(pattern.nid)}
    <#list pattern.versions as patternVersion>
    STAMP Fast      -> ${stampGetFast(patternVersion.stampNid)}
    </#list>
    -----------------
<#--    Need to do versions as well-->
<#--    Need to look into getting values out of semantics-->
<#--    Lookup via identifier, public id, uuid-->
<#--    add global TinkarTerms to reffer too in template-->
</#list>

Here are all the semantics:
<#list semantics as semantic>
    PublicId        -> ${semantic.publicId}
    Nid             -> ${semantic.nid}
    Semantic Fast   -> ${semanticGetFast(semantic.nid)}
    -----------------
</#list>