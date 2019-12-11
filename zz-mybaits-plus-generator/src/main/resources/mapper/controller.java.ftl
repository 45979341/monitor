package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import ${package.Service}.${table.serviceName};
/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 * @Author: ${author}
 * Date:     ${date}
 * Description:
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
    @Autowired
    private ${table.serviceName} ${table.serviceName?substring(0,1)?lower_case}${table.serviceName?substring(1)};
</#if>
}
</#if>
