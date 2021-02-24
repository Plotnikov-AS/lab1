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
    <table>
        <tr>
            <td>Название товара:</td>
            <td><input type="text" id="productName" placeholder="Название товара"></td>
        </tr>
        <tr>
            <td>Цена товара:</td>
            <td><input type="number" step="0.01" min="0" id="productPrice" placeholder="Цена товара" onchange="onPriceChange(this)"></td>
        </tr>
        <tr>
            <td>Кол-во товара:</td>
            <td><input type="number" min="0" id="productCount" placeholder="Кол-во товара" onchange="onCountChange(this)"></td>
        </tr>
    </table>

    <form action="/addProduct" method="post">
        <input type="hidden" id="product" name="product">
        <ul class="menu">
            <input id="addProduct" type="submit" value="Сохранить">
        </ul>
        <script>
            const addProduct = document.getElementById('addProduct');
            addProduct.onclick = function (event) {
                let price = document.getElementById('productPrice').value
                let count = document.getElementById('productCount').value
                console.log(price, count)
                if (price < 0) {
                    alert('Цена не может быть отрицательной');
                    return false;
                } else if (count < 0) {
                    alert('Количество не может быть отрицательным');
                    return false;
                } else {
                    getProduct()
                    return true;
                }
            };

            function onCountChange(count) {
                if (count.value < 0) {
                    count.style.cssText = "color: red; border: 1px solid black";
                    addProduct.disabled = true;
                    alert('Количество не может быть отрицательным');
                } else {
                    count.style.cssText = "color: black; border: 1px solid black";
                    let price = document.getElementById('productPrice').value
                    if (price >= 0) {
                        addProduct.disabled = false;
                    }
                }
            }

            function onPriceChange(price) {
                if (price.value < 0) {
                    price.style.cssText = "color: red; border: 1px solid black";
                    addProduct.disabled = true;
                    alert('Цена не может быть отрицательной');
                } else {
                    price.style.cssText = "color: black; border: 1px solid black";
                    let count = document.getElementById('productCount').value
                    if (count >= 0) {
                        addProduct.disabled = false;
                    }
                }
            }
        </script>
    </form>

    <ul class="menu">
        <li><a href="/addProduct">Добавить еще один товар</a></li>
        <li><a href="/getAllProducts">Все товары</a></li>
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>
</@p.page>
