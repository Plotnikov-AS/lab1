<#import "../macro/page.ftlh" as p>
<@p.page>
    <script>
        function getClient(clientId) {
            const jsonClient = {};
            const tr = document.getElementById("tr:" + clientId);
            const lastName = tr.getElementsByTagName('td')['clientLastName'].innerHTML.toString();
            const firstName = tr.getElementsByTagName('td')['clientFirstName'].innerHTML.toString();
            const middleName = tr.getElementsByTagName('td')['clientMiddleName'].innerHTML.toString();
            const organizationName = tr.getElementsByTagName('td')['organizationName'].innerHTML.toString();
            const client = tr.getElementsByTagName('td')['client'].getElementsByTagName('input')['client'];

            jsonClient.id = clientId
            jsonClient.lastName = lastName
            jsonClient.firstName = firstName
            jsonClient.middleName = middleName
            jsonClient.organizationName = organizationName

            client.value = JSON.stringify(jsonClient);
        }
    </script>
    <#if (clients?size > 0)>
        <h1>Клиенты</h1>
        <table>
            <tr>
                <th>Фамилия</th>
                <th>Имя</th>
                <th>Отчество</th>
                <th>Имя организации</th>
                <th>№ Лицевого счета</th>
                <th>Действие</th>
            </tr>
            <#foreach client in clients>
                <tr id="tr:${client.getId()}">
                    <td id="clientLastName">${client.getLastName()}</td>
                    <td id="clientFirstName">${client.getFirstName()}</td>
                    <td id="clientMiddleName">${client.getMiddleName()}</td>
                    <td id="organizationName">${client.getOrganizationName()}</td>
                    <td>${client.getFinAccount().getId()}</td>
                    <td id="client">
                        <form action="/getClient" method="post">
                            <input type="hidden" id="client" name="client"/>
                            <input type="submit" value="Выбрать" onclick="getClient(${client.getId()})">
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