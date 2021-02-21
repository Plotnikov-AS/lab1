<#import "../macro/page.ftlh" as p>
<@p.page>
    <h1>Добавлен новый товар</h1>
    <div>
        Название товара: ${product.getProductName()}<br>
        Количество товара: ${product.getCountLeft()}<br>
        Цена товара: ${product.getProductPrice()}<br>
    </div>
    <a href="/addProduct">Добавить еще один товар</a><br>
    <a href="/getAllProducts">Все товары</a>
    <a href="/">На главную</a>
</@p.page>
