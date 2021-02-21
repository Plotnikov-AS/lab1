<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Добавление нового товара</h1>
    <script>
        function getProduct() {
            const jsonProduct = {};
            jsonProduct.productName = document.getElementById("productName").value;
            jsonProduct.productPrice = document.getElementById("productPrice").value;
            jsonProduct.countLeft = document.getElementById("productCount").value;
            document.getElementById("product").value = JSON.stringify(jsonProduct);
        }
    </script>
    <div>
        <input type="text" id="productName" placeholder="Название товара"><br>
        <input type="text" id="productPrice" placeholder="Цена товара"><br>
        <input type="text" id="productCount" placeholder="Кол-во товара"><br>
    </div>
    <form action="/addProduct" method="post">
        <input type="hidden" id="product" name="product">
        <input type="submit" onclick="getProduct()">
    </form>
    <a href="/getAllProducts">Все товары</a>
    <a href="/">На главную</a>
</@p.page>
