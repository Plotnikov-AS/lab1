<#import "../macro/page.ftlh" as p>
<@p.page>
    <#if (clients?size > 0)>
        <table>
            <tr>
                <th>Фамилия</th>
                <th>Имя</th>
                <th>Отчество</th>
                <th>Имя организации</th>
                <th>№ Лицевого счета</th>
                <th>Комментарий</th>
                <th>Действие</th>
            </tr>
            <#foreach client in clients>
                <tr>
                    <td>${client.getLastName()}</td>
                    <td>${client.getFirstName()}</td>
                    <td>${client.getMiddleName()}</td>
                    <td>${client.getOrganizationName()}</td>
                    <td>${client.getFinAccount().getId()}</td>
                    <td>${client.getFinAccount().getComment()}</td>
                    <td>
                        <form action="getClient" method="post">
                            <input type="hidden" name="id" value="${client.getId()}"/>
                            <button type="submit">Выбрать</button>
                        </form>
                    </td>
                </tr>
            </#foreach>
        </table>
    <#else> Клиентов не найдено </#if>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>