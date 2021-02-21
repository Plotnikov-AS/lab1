<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Изменение товара</h1>
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
        <table>
            <tr>
                <td>Наименование товара:</td>
                <td><input type="text" id="productName" value="${product.getProductName()}"></td>
            </tr>
            <tr>
                <td>Цена товара:</td>
                <td><input type="number" min="0" step="0.01" id="productPrice" value="${product.getProductPrice()}">
                </td>
            </tr>
            <tr>
                <td>Количество товара:</td>
                <td><input type="number" min="0" id="productCount" value="${product.getCountLeft()}"></td>
            </tr>
        </table>
    </div>
    <form action="/changeProduct" method="post">
        <input type="hidden" id="product" name="product">
        <ul class="menu">
            <li><input type="submit" value="Сохранить" onclick="getProduct()"></li>
        </ul>
    </form>
    <ul class="menu">
        <li><a href="/getAllProducts">Все товары</a></li>
        <li><a href="/">На главную</a></li>
    </ul>

</@p.page>
