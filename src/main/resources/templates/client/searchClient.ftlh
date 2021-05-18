<#import "../macro/page.ftlh" as p>

<@p.page>
<div>
    <h1>Поиск клиента</h1>
    <script>
        function getClient() {
            const jsonClient = {};
            jsonClient.lastName = document.getElementById("clientLastName").value;
            jsonClient.firstName = document.getElementById("clientFirstName").value;
            jsonClient.middleName = document.getElementById("clientMiddleName").value;
            jsonClient.organizationName = document.getElementById("organizationName").value;

            document.getElementById("client").value = JSON.stringify(jsonClient);
        }
    </script>
    <div>
        <table>
            <tr>
                <td>Фамилия клиента:</td>
                <td><input type="text" id="clientLastName" placeholder="Фамилия клиента"></td>
            </tr>
            <tr>
                <td>Имя клиента:</td>
                <td><input type="text" id="clientFirstName" placeholder="Имя клиента"></td>
            </tr>
            <tr>
                <td>Отчество клиента:</td>
                <td><input type="text" id="clientMiddleName" placeholder="Отчество клиента"></td>
            </tr>
            <tr>
                <td>Название организации:</td>
                <td><input type="text" id="organizationName" placeholder="Название организации"></td>
            </tr>
        </table>
    </div>
    <form action="/getClient" method="post">
        <input type="hidden" id="client" name="client">
        <ul class="menu">
            <li><input type="submit" value="Найти" onclick="getClient()"></li>
            <li><a href="javascript:history.back()">Назад</a></li>
            <li><a href="/">На главную</a></li>
        </ul>
    </form>
    </@p.page>
