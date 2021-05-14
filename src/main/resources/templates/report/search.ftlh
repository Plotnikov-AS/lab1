<#import "../macro/page.ftlh" as p>

<@p.page>
    <script>
        function getParams() {
            document.getElementById("dateFromParam").value = document.getElementById("dateFrom").value;
            document.getElementById("dateToParam").value = document.getElementById("dateTo").value;
            document.getElementById("paymentTypeParam").value = document.getElementById("paymentType").value;
        }
    </script>
    <h1>Поиск продажи</h1>
    <table id="search">
        <tr>
            <td>Тип продажи:</td>
            <td><select id="paymentType">
                    <option value="cash">Наличный</option>
                    <option value="creditCard">Безналичный</option>
                    <option value="backReturn">Возврат</option>
                    <option value="all">Все</option>
                </select></td>
        </tr>
        <tr>
            <td>С:</td>
            <td><input type="datetime-local" step="1" id="dateFrom"></td>
        </tr>
        <tr>
            <td>По:</td>
            <td><input type="datetime-local" step="1" id="dateTo"></td>
        </tr>
        <tr>
            <td>
                <div>
                    <form action="/findReports" method="post">
                        <input type="hidden" id="dateFromParam" name="dateFrom">
                        <input type="hidden" id="dateToParam" name="dateTo">
                        <input type="hidden" id="paymentTypeParam" name="paymentType">
                        <input type="submit" onclick="getParams()" value="Найти продажи">
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
