<#list semantics as semantic>
    PublicId        -> ${semantic.publicId}
    Nid             -> ${semantic}
    Semantic Fast   -> ${semantic}
    <#list semantic.versions as semanticVersion>
        Semantic Version STAMP Fast      -> ${entityGet(semanticVersion.stampNid)}
    </#list>
    <#assign latestVersion = latestVersionOf(semantic, defaultSTAMPCalc)>
    <#if latestVersion.isPresent() == true>
    Semantic Latest Version -> ${latestVersion.get()}
    </#if>
    -----------------
</#list>
