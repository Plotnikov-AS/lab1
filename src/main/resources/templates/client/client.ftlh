<#import "../macro/page.ftlh" as p>

<@p.page>
    <script>
        function getPaymentType() {
            document.getElementById('paymentType').value = document.getElementById('paymentTypeSel').value
        }
    </script>
    <h1>Клиент</h1>
    <table>
        <tr>
            <td>Клиент:</td>
            <td>${client.getLastName()} ${client.getFirstName()} ${client.getMiddleName()}
                (${client.getOrganizationName()})
            </td>
        </tr>
        <tr>
            <td>Номер лицевого счета клиента:</td>
            <td>${client.getFinAccount().getId()}</td>
        </tr>
        <tr>
            <td>Сумма покупок клиента:</td>
            <td>${client.getFinAccount().getTotalSpent()} $</td>
        <tr>
            <td>Комментарий:</td>
            <td>${client.getComment()}</td>
        </tr>
        <tr>
            <td>Способ оплаты:</td>
            <td><select id="paymentTypeSel">
                    <#list paymentTypes as paymentType>
                        <option >${paymentType}</option>
                    </#list>
                </select></td>
        </tr>
    </table>
    <form action="/order" method="post">
        <input type="hidden" name="paymentType" id="paymentType">
        <input type="hidden" name="clientId" value="${client.getId()}">
        <ul class="menu">
            <li><input type="submit" value="Оформить продажу" onclick="getPaymentType()"></li>
            <li><a href="javascript:history.back()">Назад</a></li>
            <li><a href="/">На главную</a></li>
        </ul>
    </form>
</@p.page>
