<#import "../macro/page.ftlh" as p>

<@p.page>
    <script src="js/order.js"></script>
    <script>
        window.onload = function () {
            updateTotalPrice()
        };

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
    <h1>Товарный чек №${reportNumber}</h1>
    <table>
        <tr>
            <td>Номер кассового аппарата:</td>
            <td>${cassaNumber}</td>
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
                <td>${product["productName"]}</td>
                <td>${product["count"]}</td>
                <td>${product["priceEach"]}</td>
                <td id="totalPriceForPosition">
                    <div id="totalPriceForPosition">${product["priceTotal"]}</div>
                </td>
            </tr>
        </#foreach>
        <tr>
            <td>
                <div id="totalPrice"><b>Общая сумма покупки: </b></div>
            </td>
            <td>
                <div>
                    <form action="/cashPayed" method="post">
                        <input type="hidden" id="cart" name="cart" value="">
                        <input type="hidden" id="clientId" name="clientId" value="${client.getId()}">
                        <input type="submit" value="Оплачен" onclick="makeCart()">
                    </form>
                </div>
            </td>
        </tr>
    </table>
</@p.page>
