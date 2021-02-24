<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Добавлен новый товар</h1>
    <table>
        <tr>
            <td>Название товара:</td>
            <td>${product.getProductName()}</td>
        </tr>
        <tr>
            <td>Количество товара:</td>
            <td>${product.getCountLeft()} шт.</td>
        </tr>
        <tr>
            <td>Цена товара:</td>
            <td>${product.getProductPrice()} руб.</td>
        </tr>
    </table>
    <ul class="menu">
        <li><a href="/addProduct">Добавить еще один товар</a></li>
        <li><a href="/getAllProducts">Все товары</a></li>
        <li><a href="javascript:history.back()">Назад</a></li>
        <li><a href="/">На главную</a></li>
    </ul>

</@p.page>
