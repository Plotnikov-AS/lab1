<#import "../macro/page.ftlh" as p>
<@p.page>
    <script>
        var cart = [];

        function updateTotalPriceForPosition(count, tableRow) {
            var tr = document.getElementById("tr:" + tableRow);
            var productPrice = tr.getElementsByTagName('td')['productPrice'].getElementsByTagName('div')['productPrice'].innerHTML.toString().replace(/&nbsp;/g, '');
            tr.getElementsByTagName('td')['totalPriceForPosition'].getElementsByTagName('div')['totalPriceForPosition'].innerHTML = (productPrice * count.value).toString();
        }

        function updateTotalPrice() {
            var trs = document.getElementById('products').getElementsByTagName('tr');
            var totalPrice = 0;
            for (var i = 0; i < trs.length; i++) {
                var totalPriceForPosition = trs[i].getElementsByTagName('div')['totalPriceForPosition'];
                if (totalPriceForPosition !== undefined) {
                    totalPrice += parseInt(totalPriceForPosition.innerHTML);
                }
            }
            document.getElementById('totalPrice').innerHTML = '<b>Общая сумма покупки:</b> ' + totalPrice.toString() + ' руб.';
        }

        function updateCart(count, productId) {
            updateTotalPriceForPosition(count, productId);
            updateTotalPrice();
            if (count.value > 0) {
                const product = new Map();
                if (cart.length === 0) {
                    product.set('productId', productId);
                    product.set('count', count.value);
                    cart.push(product);
                } else {
                    for (let i = 0; i < cart.length; i++) {
                        let curProductId = cart[i].get('productId');
                        if (curProductId === productId) {
                            cart[i].set('count', count.value);
                            break;
                        }
                        if (i === cart.length - 1) {
                            product.set('productId', productId);
                            product.set('count', count.value);
                            cart.push(product);
                        }
                    }
                }
            }
        }

        function getCart() {
            const jsonCart = {};
            jsonCart.products = arrayOfMapsToArrayOfObjs(cart);
            document.getElementById("cart").value = JSON.stringify(jsonCart);
        }

        function arrayOfMapsToArrayOfObjs(array) {
            const obj = []
            for (let i = 0; i < array.length; i++) {
                let value = array[i];
                let innerObj = {}
                if (value instanceof Map) {
                    for (let [k, v] of value) {
                        innerObj[k] = v;
                    }
                    obj.push(innerObj);
                }
            }
            return obj
        }


    </script>
    <h1>Продажа</h1>
    <label>Тип оплаты: ${paymentType}</label>
    <label>Баланс клиента: ${client.getFinAccount().getCurBalance()} руб.</label>
    <div>
        <p><b>Товары</b><br></p>
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
    <div id="totalPrice"><b>Общая сумма покупки: </b>0.0 руб.</div>
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
