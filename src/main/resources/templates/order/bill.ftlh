<#import "../macro/page.ftlh" as p>

<@p.page>
    <script src="js/order.js"></script>
    <script>
        window.onload = function () {
            updateTotalPrice()
        };

        function makeCart() {
            let prod;
            <#foreach prod in cart>
            prod = new Map();
            prod.set('productId', ${prod["productId"]})
            prod.set('count', ${prod["count"]})
            cart.push(prod);
            console.log(cart);
            </#foreach>
            getCart();
        }
    </script>
    <h1>Счет на оплату</h1>
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
                    <form action="/creditPayed" method="post">
                        <input type="hidden" id="cart" name="cart" value="">
                        <input type="hidden" id="clientId" name="clientId" value="${client.getId()}">
                        <input type="submit" value="Оплачен" onclick="makeCart()">
                    </form>
                </div>
            </td>
        </tr>
    </table>
</@p.page>
