<#import "../macro/page.ftlh" as p>
<@p.page>
    <script>function getTotalPrice() {
            let total = 0.0;
            let positions = []
            let tds = document.getElementsByTagName('td');
            for (let i = 0; i < tds.length; i++) {
                let td = tds[i];
                if (td.id === 'priceTotal') {
                    positions.push(td);
                }
            }
            for (let i = 0; i < positions.length; i++) {
                let position = positions[i];
                total += parseFloat(position.innerHTML)
            }
            document.write("<p><b>Общая сумма заказа: " + total.toFixed(2) + "</b></p>")
        }
    </script>
    <h1>Результат выполнения заказа</h1>
    <h3>Статус: ${result.getStatus()}</h3>
    <#if (result.getStatus() == "Ошибка")>
        Заказ не выполнен<br>
        ${result.getMessage()}<br>
    <#else>
    <div>
        <p><b>Заказ:</b></p>
        <table>
            <tr>
                <th>Наименование товара</th>
                <th>Кол-во</th>
                <th>Цена за ед.</th>
                <th>Общая стоимость</th>
            </tr>
            <#foreach product in cart>
                <tr>
                    <td>${product['productName']}</td>
                    <td>${product['count']}</td>
                    <td>${product['priceEach']}</td>
                    <td id="priceTotal">${product['priceTotal']}</td>
                </tr>
            </#foreach>
        </table>
        <div id="totalPrice">
            <script>getTotalPrice()</script>
        </div>
    </div>
    </#if>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
