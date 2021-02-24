<#import "../macro/page.ftlh" as p>

<@p.page>
    <script>
        function getPaymentType() {
            document.getElementById('paymentType').value = document.getElementById('paymentTypeSel').value
            console.log(document.getElementById('paymentTypeSel').value)
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
            <td>Текущий баланс клиента:</td>
            <td>${client.getFinAccount().getCurBalance()}</td>
        </tr>
        <tr>
            <td>Сумма покупок клиента:</td>
            <td>${client.getFinAccount().getTotalSpent()}</td>
        </tr>
        <tr>
            <td>Остаток кредита:</td>
            <td id="creditLeft">${client.getFinAccount().getCreditLeft()}</td>

        </tr>
        <tr>
            <td>Текущий долг по кредиту:</td>
            <td id="curDebt">${client.getFinAccount().getCurDebt()}</td>
        </tr>
        <tr>
            <td>Максимальный кредит:</td>
            <td id="maxCredit">${client.getFinAccount().getCreditMax()}</td>
        </tr>
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
        <script>
            window.onload = checkCreditLeft;

            function checkCreditLeft() {
                let creditMax = document.getElementById('maxCredit')
                let curDebt = document.getElementById('curDebt')
                let creditLeft = document.getElementById('creditLeft')

                if (creditLeft.innerText <= (creditMax.innerText * 0.1)) {
                    curDebt.style.cssText = "color: red; border: 1px solid black";
                    creditLeft.style.cssText = "color: red; border: 1px solid black";
                }
            }
        </script>
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
