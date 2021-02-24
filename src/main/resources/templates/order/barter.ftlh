<#import "../macro/page.ftlh" as p>
<@p.page>
    <script src="js/order.js"></script>
    <h1>Бартер</h1>
    <#if (products?size <= 0)>
        <h2>Склад пуст!</h2>
    <#else>
        <table>
            <tr>
                <th>Товары на складе</th>
                <th>Товары клиента</th>
            </tr>
            <tr>
                <td>
                    <table id="products">
                        <tr>
                            <th>Наименование товара</th>
                            <th>Кол-во</th>
                            <th>Остаток на складе</th>
                        </tr>
                        <#foreach product in products>
                            <tr id="tr:${product.getId()}">
                                <td>${product.getProductName()}</td>
                                <td><input type="number" id="count" min="0" max="${product.getCountLeft()}"
                                           onchange="onCountChange(this, ${product.getId()}, ${product.getCountLeft()})">
                                </td>
                                <td>${product.getCountLeft()}</td>
                            </tr>
                        </#foreach>
                    </table>
                </td>
                <td>
                    <table id="clientProducts">
                        <tr>
                            <th>Наименование товара</th>
                            <th>Кол-во</th>
                        </tr>
                        <tr id="clientProduct">
                            <td id="productName"><input type="text" placeholder="Название товара"></td>
                            <td id="productCount"><input type="number" min="0" placeholder="Кол-во товара" onchange="updateClientProducts(this)"></td>
                        </tr>
                        <tr>
                            <td><input type="submit" value="Добавить строку" onclick="addNewTableRow()"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <form action="/sale" method="post">
            <input type="hidden" id="clientProductsJson" name="clientProductsJson">
            <input type="hidden" id="cart" name="cart">
            <input type="hidden" name="clientId" value="${client.getId()}">
            <input type="hidden" name="paymentType" value="${paymentType}">
            <ul class="menu">
                <li><input type="submit" id="submitOrder" value="Отправить заказ"></li>
                <li><a href="javascript:history.back()">Назад</a></li>
                <li><a href="/">На главную</a></li>
            </ul>
            <script>
                const submitOrder = document.getElementById('submitOrder');
                submitOrder.onclick = function (event) {
                    if (cart.length === 0 || allProductsInCartIsZeroCount()) {
                        alert('Корзина пуста');
                        return false;
                    } else if (clientProducts === 0){
                        alert('Клиент не предоставил товары')
                        return false;
                    } else {
                        getCartAndClientProducts()
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
</@p.page>
