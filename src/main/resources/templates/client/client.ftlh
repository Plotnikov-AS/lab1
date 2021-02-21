<#import "../macro/page.ftlh" as p>

<@p.page>
    <label>Клиент: ${client.getLastName()} ${client.getFirstName()} ${client.getMiddleName()} ${client.getOrganizationName()}</label>
    <br>
    <label>Номер лицевого счета клиента: ${client.getFinAccount().getId()}</label><br>
    <label>Текущий баланс клиента: ${client.getFinAccount().getCurBalance()}</label><br>
    <label>Сумма покупок клиента: ${client.getFinAccount().getTotalSpent()}</label><br>
    <label>Остаток кредита: ${client.getFinAccount().getCreditLeft()}</label><br>
    <label>Текущий долг по кредиту: ${client.getFinAccount().getCurDebt()}</label><br>
    <label>Максимальный кредит: ${client.getFinAccount().getCreditMax()}</label><br>
    <label>Комментарий: ${client.getComment()}</label><br>
    <form action="/order" method="post">
        <select name="paymentType">
            <#list paymentTypes as paymentType>
                <option value="${paymentType}">${paymentType}</option>
            </#list>
        </select>
        <input type="hidden" name="clientId" value="${client.getId()}">
        <ul class="menu">
            <li><input type="submit" value="Оформить продажу"></li>
        </ul>
    </form>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
