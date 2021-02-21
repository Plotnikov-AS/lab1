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
        <input type="text" id="clientLastName" placeholder="Фамилия клиента"><br>
        <input type="text" id="clientFirstName" placeholder="Имя клиента"><br>
        <input type="text" id="clientMiddleName" placeholder="Отчество клиента"><br>
        <input type="text" id="organizationName" placeholder="Название организации"><br>
    </div>
    <form action="/getClient" method="post">
        <input type="hidden" id="client" name="client">
        <ul class="menu">
            <li><input type="submit" value="Найти" onclick="getClient()"></li>
        </ul>
    </form>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
    </@p.page>
