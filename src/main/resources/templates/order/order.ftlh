<#import "../macro/page.ftlh" as p>
<@p.page>
    <script src="js/order.js"></script>
    <h1>Продажа</h1>
    <label>Тип оплаты: ${paymentType}</label>
    <div>
        <#if (products?size <= 0)>
        <h2>Склад пуст!</h2>
        <#else>
        <table id="products">
            <tr>
                <th>Наименование товара</th>
                <th>Кол-во</th>
                <th>Цена за шт.</th>
                <th>Остаток на складе</th>
                <th>Общая стоимость</th>
            </tr>
            <#foreach product in products>
                <tr id="tr:${product.getId()}">
                    <td>${product.getProductName()}</td>
                    <td><input type="number" id="count" min="0" max="${product.getCountLeft()}"
                               onchange="onCountChange(this, ${product.getId()}, ${product.getCountLeft()})">
                    </td>
                    <td id="productPrice">
                        <div id="productPrice">${product.getProductPrice()}</div>
                    </td>
                    <td>${product.getCountLeft()}</td>
                    <td id="totalPriceForPosition">
                        <div id="totalPriceForPosition">0.0</div>
                    </td>
                </tr>
            </#foreach>
        </table>
        <br>
    </div>
    <div id="totalPrice"><b>Общая сумма покупки: </b>0.0 $</div>
    <form action="/sale" method="post">
        <input type="hidden" id="cart" name="cart">
        <input type="hidden" name="paymentType" value="${paymentType}">
        <input type="hidden" name="clientId" value="${client.getId()}">
        <ul class="menu">
            <li><input id="submitOrder" type="submit" value="Выполнить заказ"></li>
        </ul>
        <script>
            const submitOrder = document.getElementById('submitOrder');
            submitOrder.onclick = function (event) {
                if (cart.length === 0) {
                    alert('Корзина пуста');
                    return false;
                } else {
                    getCart();
                    return true;
                }
            };

            function onCountChange(count, productId, maxCount) {
                if (count.value > maxCount) {
                    count.style.cssText = "color: red; border: 1px solid black";
                    submitOrder.disabled = true;
                    alert('Превышено максимальное кол-во товара');
                } else if (count.value < 0) {
                    count.style.cssText = "color: red; border: 1px solid black";
                    submitOrder.disabled = true;
                    alert('Количество не может быть отрицательным');
                } else {
                    count.style.cssText = "color: black; border: 1px solid black";
                    submitOrder.disabled = false;
                    updateCart(count, productId);
                }
            }
        </script>
    </form>
</#if>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
