<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Товар</h1>
    <script>
        function getProduct() {
            const jsonProduct = {};
            jsonProduct.productName = document.getElementById("productName").value;
            jsonProduct.productPrice = document.getElementById("productPrice").value.toString().replace(/\s/g, '');
            jsonProduct.countLeft = document.getElementById("productCount").value.toString().replace(/\s/g, '');
            document.getElementById("product").value = JSON.stringify(jsonProduct);
        }
    </script>
    <div>
        <input type="hidden" id="productName" value="${product.getProductName()}">
        <input type="hidden" id="productPrice" value="${product.getProductPrice()}">
        <input type="hidden" id="productCount" value="${product.getCountLeft()}">
        Название товара: ${product.getProductName()}<br>
        Цена товара: ${product.getProductPrice()}<br>
        Количество товара: ${product.getCountLeft()}<br>
    </div>
    <form action="/changeProduct" method="get">
        <input type="hidden" id="product" name="product">
        <ul class="menu">
            <li><input type="submit" value="Изменить" onclick="getProduct()"></li>
        </ul>

    </form>
    <ul class="menu">
        <li><a href="/getAllProducts">Все товары</a></li>
        <li><a href="/">На главную</a></li>
    </ul>

</@p.page>
