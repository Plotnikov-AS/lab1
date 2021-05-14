<#import "../macro/page.ftlh" as p>

<@p.page>
    <h1>Продажи</h1>
    <table id="sales">
        <tr>
            <th>Вид продажи</th>
            <th>Дата продажи</th>
            <th>Сумма</th>
        </tr>
        <#list sales?keys as saleType>
            <#assign firstRowInSaleTypes = true>
            <#list sales[saleType]?keys as saleDate>
                <#foreach sale in sales[saleType][saleDate]>
                    <#if firstRowInSaleTypes == true>
                        <#if saleType == "cash">
                            <tr>
                                <td>
                                    <b>НАЛИЧНЫЙ РАСЧЕТ</b><br>
                                    &emsp;Всего
                                </td>
                                <td></td>
                                <td>
                                    ${totalCash}
                                </td>
                            </tr>
                            <#assign firstRowInSaleTypes = false>
                        <#elseif saleType == "creditCard">
                            <tr>
                                <td>
                                    <b>БЕЗНАЛИЧНЫЙ РАСЧЕТ</b><br>
                                    &emsp;Всего
                                </td>
                                <td></td>
                                <td>${totalCredit}</td>
                            </tr>
                            <#assign firstRowInSaleTypes = false>
                        <#elseif saleType == "backReturn">
                            <tr>
                                <td>
                                    <b>ВОЗВРАТЫ ТОВАРА</b><br>
                                    &emsp;Всего
                                </td>
                                <td></td>
                                <td>
                                    ${totalReturns}
                                </td>
                            </tr>
                            <#assign firstRowInSaleTypes = false>
                        </#if>
                    </#if>
                    <#if sale_index == 0>
                        <tr>
                            <td>
                                <i>За день</i>
                            </td>
                            <td>
                                ${saleDate}
                            </td>
                            <td>
                                ${totalByDays[saleType][saleDate]}
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td>
                            <#if saleType == "cash"> Товарный чек №${sale.getNumber()}
                            <#elseif saleType == "creditCard"> Продажа за безналичный расчет №${sale.getNumber()}
                            <#elseif saleType == "backReturn"> Возврат товара №${sale.getNumber()}
                            </#if>
                        </td>
                        <td>
                            ${sale.getDate()}
                        </td>
                        <td>
                            ${sale.getAmount()}
                        </td>
                    </tr>
                </#foreach>
            </#list>
        </#list>
    </table>

    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>