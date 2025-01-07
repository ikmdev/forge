Here are all the concept Nids:
<#list entities as entity>
    ${entity.nid}
    ${description(entity.nid)}
    <#list parent(entity.nid) as parentNid>
        Parent -> ${description(parentNid)}
    </#list>
</#list>