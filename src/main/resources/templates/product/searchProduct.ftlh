<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Поиск товара</h1>
    <script>
        function getProduct() {
            const jsonProduct = {};
            jsonProduct.productName = document.getElementById("productName").value;
            document.getElementById("product").value = JSON.stringify(jsonProduct);
        }
    </script>
    <div>
        <input type="text" id="productName" placeholder="Название товара"><br>
    </div>
    <form action="/getProduct" method="post">
        <input type="hidden" id="product" name="product">
        <ul class="menu">
            <li><input type="submit" value="Найти" onclick="getProduct()"></li>
        </ul>
    </form>
    <ul class="menu">
        <li><a href="/getAllProducts">Все товары</a></li>
        <li><a href="/">На главную</a></li>
    </ul>

</@p.page>
