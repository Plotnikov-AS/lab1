<#import "../macro/page.ftlh" as p>
<@p.page>
    <script src="js/order.js"></script>
    <script>
        window.onload = function () {
            var trs = document.getElementById('products').getElementsByTagName('tr');
            var totalPrice = 0;
            for (var i = 0; i < trs.length; i++) {
                var totalPriceForPosition = trs[i].getElementsByTagName('div')['totalPriceForPosition'];
                if (totalPriceForPosition !== undefined) {
                    totalPrice += parseInt(totalPriceForPosition.getElementsByTagName('input')[0].value);
                }
            }
            if (document.getElementById('totalPrice') !== null) {
                document.getElementById('totalPrice').value = totalPrice.toString();
            }
        };
    </script>
    <script>
        function makeCart() {
            let prod = new Map();
            <#foreach prod in cart>
            prod.set('productId', ${prod["productId"]})
            prod.set('count', ${prod["count"]})
            cart.push(prod);
            </#foreach>
            getCart();
            console.log(cart);
        }
    </script>
    <h1>Продажа за безналичный расчет №${reportNumber}</h1>
    <table id="client">
        <tr>
            <td><b>ФИО</b></td>
            <td>${client.getLastName()} ${client.getFirstName()} ${client.getMiddleName()}
                (${client.getOrganizationName()})
            </td>
        </tr>
    </table><br>
    <table id="products">
        <tr>
            <th>Наменование</th>
            <th>Количество</th>
            <th>Цена, шт.</th>
            <th>Сумма</th>
        </tr>
        <#foreach product in cart>
            <tr>
                <td><input type="text" value="${product["productName"]}"></td>
                <td><input type="text" value="${product["count"]}"></td>
                <td><input type="text" value="${product["priceEach"]}"></td>
                <td id="totalPriceForPosition">
                    <div id="totalPriceForPosition"><input type="text" value="${product["priceTotal"]}"></div>
                </td>
            </tr>
        </#foreach>
        <tr>
            <td>
                <b>Общая сумма покупки, $:</b>
            </td>
            <td>
                <input id="totalPrice" type="text" value="">
            </td>
        </tr>
    </table>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
