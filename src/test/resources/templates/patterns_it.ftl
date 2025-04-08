<#list patterns as pattern>
    PublicId        -> ${pattern.publicId}
    Nid             -> ${pattern}
    Desc            -> ${textOf(pattern, defaultLanguageCalc)}
    Pattern Fast    -> ${pattern}
    <#list pattern.versions as patternVersion>
        Pattern Version STAMP Fast      -> ${entityGet(patternVersion.stampNid)}
    </#list>
    <#assign latestVersion = latestVersionOf(pattern, defaultSTAMPCalc)>
    <#if latestVersion.isPresent() == true>
    Pattern Latest Version -> ${latestVersion.get()}
    </#if>
    -----------------
</#list>