var cart = [];
var clientProducts = 0;

function updateTotalPriceForPosition(count, tableRow) {
    var tr = document.getElementById("tr:" + tableRow);
    if (tr.getElementsByTagName('td')['productPrice'] !== undefined) {
        var productPrice = tr.getElementsByTagName('td')['productPrice'].getElementsByTagName('div')['productPrice'].innerHTML.toString().replace(/&nbsp;/g, '');
    }
    if (productPrice !== undefined) {
        tr.getElementsByTagName('td')['totalPriceForPosition'].getElementsByTagName('div')['totalPriceForPosition'].innerHTML = (productPrice * count.value).toString();
    }
}

function updateTotalPrice() {
    var trs = document.getElementById('products').getElementsByTagName('tr');
    var totalPrice = 0;
    for (var i = 0; i < trs.length; i++) {
        var totalPriceForPosition = trs[i].getElementsByTagName('div')['totalPriceForPosition'];
        if (totalPriceForPosition !== undefined) {
            totalPrice += parseInt(totalPriceForPosition.innerHTML);
        }
    }
    if (document.getElementById('totalPrice') !== null) {
        document.getElementById('totalPrice').innerHTML = '<b>Общая сумма покупки:</b> ' + totalPrice.toString() + ' $';
    }
}

function updateCart(count, productId) {
    updateTotalPriceForPosition(count, productId);
    updateTotalPrice();
    if (count.value > 0) {
        const product = new Map();
        if (cart.length === 0) {
            product.set('productId', productId);
            product.set('count', count.value);
            cart.push(product);
        } else {
            for (let i = 0; i < cart.length; i++) {
                let curProductId = cart[i].get('productId');
                if (curProductId === productId) {
                    cart[i].set('count', count.value);
                    break;
                }
                if (i === cart.length - 1) {
                    product.set('productId', productId);
                    product.set('count', count.value);
                    cart.push(product);
                }
            }
        }
    }
}

function allProductsInCartIsZeroCount() {
    if (cart.length > 0) {
        console.log('cart length: ', cart.length);
        for (let i = 0; i < cart.length; i++) {
            console.log('cart count: ', cart[i].get('count'));
            if (cart[i].get('count') > 0) {
                return false;
            }
        }
    }
    console.log('cart length: ', cart.length);
    return true;
}

function updateClientProducts(clientProduct) {
    clientProducts += clientProduct.value
}

function getCart() {
    const jsonCart = {};
    jsonCart.products = arrayOfMapsToArrayOfObjs(cart);
    document.getElementById("cart").value = JSON.stringify(jsonCart);
}

function arrayOfMapsToArrayOfObjs(array) {
    const obj = []
    for (let i = 0; i < array.length; i++) {
        let value = array[i];
        let innerObj = {}
        if (value instanceof Map) {
            for (let [k, v] of value) {
                innerObj[k] = v;
            }
            obj.push(innerObj);
        }
    }
    return obj
}

function addNewTableRow() {
    let table = document.getElementById('clientProducts');
    let newRow = table.insertRow(2);
    newRow.setAttribute('id', 'clientProduct')
    let newCell = newRow.insertCell(0);
    newCell.setAttribute('id', 'productName');
    let newElem = document.createElement('input');
    newElem.setAttribute('type', 'text');
    newElem.setAttribute('placeholder', 'Название товара');
    newCell.appendChild(newElem);

    newCell = newRow.insertCell(1);
    newCell.setAttribute('id', 'productCount');
    newElem = document.createElement('input');
    newElem.setAttribute('type', 'number');
    newElem.setAttribute('min', '0');
    newElem.setAttribute('placeholder', 'Кол-во товара');
    newCell.appendChild(newElem);
}

function getCartAndClientProducts() {
    let productsJson = {}
    let productsArray = []
    let trs = document.getElementById('clientProducts').getElementsByTagName('tr')
    for (let i = 0; i < trs.length; i++) {
        let tr = trs[i]
        if (tr.id === 'clientProduct') {
            console.log(tr.getElementsByTagName('td'))
            let prodName = tr.getElementsByTagName('td')['productName'].getElementsByTagName('input')[0].value
            let prodCount = tr.getElementsByTagName('td')['productCount'].getElementsByTagName('input')[0].value
            let innerObj = {};
            innerObj.productName = prodName;
            innerObj.count = prodCount;
            productsArray.push(innerObj)
        }
    }
    productsJson.products = productsArray;
    document.getElementById("clientProductsJson").value = JSON.stringify(productsJson);
    getCart()
    console.log('clientProductsJson', document.getElementById("clientProductsJson").value)
    console.log('cart', document.getElementById("cart").value)
}

