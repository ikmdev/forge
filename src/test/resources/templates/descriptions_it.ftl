<#list concepts as concept>
    Concept: ${concept}
    Description Data:
    <#list descriptionsOn(concept, defaultSTAMPCalc) as description>
        language: ${description.language}
        Text: ${description.text}
        Case Significance: ${description.caseSignificance}
        Type: ${description.type}
    </#list>

</#list>
