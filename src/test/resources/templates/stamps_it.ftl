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
    <#assign latestVersion = latestVersionOf(stamp, defaultSTAMPCalc)>
    <#if latestVersion.isPresent() == true>
    STAMP Latest Version ->    ${latestVersion.get()}
    </#if>
    -----------------
</#list>