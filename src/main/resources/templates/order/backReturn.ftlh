<#import "../macro/page.ftlh" as p>

<@p.page>
    <script>
        function getReportInfo() {
            document.getElementById("sellDate").value = document.getElementById("sellDateRep").value;
            document.getElementById("reportNumber").value = document.getElementById("reportIdRep").value;
        }
    </script>
    <h1>Возврат товара</h1>
    <table id="client">
        <tr>
            <td>Покупатель:</td>
            <td>${client.getLastName()} ${client.getFirstName()} ${client.getMiddleName()} ${client.getOrganizationName()}</td>
        </tr>
        <tr>
            <td>Дата продажи:</td>
            <td><input type="datetime-local" id="sellDateRep" step="1"></td>
        </tr>
        <tr>
            <td>Номер документа продажи:</td>
            <td><input type="number" id="reportIdRep"></td>
        </tr>
        <tr>
            <td>
                <div>
                    <form action="/getProductsInReport" method="post">
                        <input type="hidden" id="sellDate" name="sellDate">
                        <input type="hidden" id="reportNumber" name="reportNumber">
                        <input type="hidden" name="clientId" value="${client.getId()}">
                        <input type="submit" value="Найти" onclick="getReportInfo()">
                    </form>
                </div>
            </td>
        </tr>
    </table>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
