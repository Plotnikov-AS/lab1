<#import "../macro/page.ftlh" as p>

<@p.page>
    <script src="js/order.js"></script>
    <script>
        var cart = [];

        function makeReturnCart() {
            let product = new Map();
            let trs = document.getElementById("products").getElementsByTagName("tr");
            for (let i = 0; i < trs.length; i++) {
                let tr = trs[i];
                if (tr.id !== "" && tr.id !== "subm") {
                    let tds = tr.getElementsByTagName("td");
                    let input = tds['toReturn'].getElementsByTagName("input")[0];
                    if (input.checked) {
                        product.set('productId', tr.id);
                        product.set('count', tds['count'].getElementsByTagName("input")[0].value)
                        cart.push(product);
                    }
                }
            }
            getCart();
        }
    </script>
    <h1>Возврат товара</h1>
    <table id="products">
        <tr>
            <th>Наименование</th>
            <th>Количество</th>
            <th>Возврат</th>
        </tr>
        <#foreach product in products>
            <tr id="${product.getId()}">
                <td id="prodId">${product.getProductName()}</td>
                <td id="count"><#foreach rep in report><#if product.getId() == rep.getProductId()><input type="number" value="${rep.getCount()}" max="${rep.getCount()}"></#if></#foreach></td>
                <td id="toReturn"><input type="checkbox"></td>
            </tr>
        </#foreach>
        <tr id="subm">
            <td>
                <div>
                    <form method="post" action="/doReturn">
                        <input type="hidden" id="cart" name="cart">
                        <input type="hidden" id="clientId" name="clientId" value="${client.getId()}">
                        <input type="submit" value="Оформить" onclick="makeReturnCart()">
                    </form>
                </div>
            </td>
        </tr>
    </table>
    <ul class="menu">
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
