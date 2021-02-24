<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Список товаров</h1>
    <script>
        function getProduct(tableRow) {
            const tr = document.getElementById("tr:" + tableRow);
            const productName = tr.getElementsByTagName('td')['productName'].innerHTML.toString();
            const countLeft = tr.getElementsByTagName('td')['countLeft'].innerHTML.toString().replace(/&nbsp;/g, '');
            const productPrice = tr.getElementsByTagName('td')['productPrice'].innerHTML.toString().replace(/&nbsp;/g, '');
            const product = tr.getElementsByTagName('td')['product'].getElementsByTagName('input')['product'];

            const jsonProduct = {};
            jsonProduct.productName = productName;
            jsonProduct.productPrice = productPrice;
            jsonProduct.countLeft = countLeft;
            product.value = JSON.stringify(jsonProduct);
        }
    </script>
    <#if (products?size <= 0)>
        <h2>Склад пуст!</h2>
    <#else>
        <table id="products">
            <tr>
                <th>Наименование товара</th>
                <th>Остаток на складе</th>
                <th>Цена за шт.</th>
                <th></th>
            </tr>
            <#foreach product in products>
                <tr id="tr:${product.getId()}">
                    <td id="productName">${product.getProductName()}</td>
                    <td id="countLeft">${product.getCountLeft()}</td>
                    <td id="productPrice">${product.getProductPrice()}</td>
                    <td id="product">
                        <form action="/changeProduct" method="get">
                            <input type="hidden" id="product" name="product">
                            <input type="submit" value="Изменить" onclick="getProduct(${product.getId()})">
                        </form>
                    </td>
                </tr>
            </#foreach>
        </table>
    </#if>
    <ul class="menu">
        <li><a href="/addProduct">Добавить новый товар</a></li>
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>

</@p.page>
