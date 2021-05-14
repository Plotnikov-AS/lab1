<#import "../macro/page.ftlh" as p>

<@p.page>
    <h1>Возврат товара №${reportNumber}</h1>
    <table id="client">
        <tr>
            <td>Покупатель:</td>
            <td>${client.getLastName()} ${client.getFirstName()} ${client.getMiddleName()} (${client.getOrganizationName()})</td>
        </tr>
    </table><br><br>
    <table id="products">
        <tr>
            <th>Наименование</th>
            <th>Количество</th>
        </tr>
        <#foreach product in products>
            <tr id="${product.getId()}">
                <td id="prodId">${product.getProductName()}</td>
                <td id="count"><#foreach rep in report><#if product.getId() == rep.getProduct().getId()>${rep.getCount()}</#if></#foreach></td>
            </tr>
        </#foreach>
    </table>
    <ul class="menu">
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
