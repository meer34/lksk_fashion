
function onProductChange(id) {
	var s = '<option selected disabled="disabled" value="">Item</option>';
	var unitTag = '<option selected disabled="disabled" value="">Unit</option>';
	if (id) {
		$.ajax({
			url : '/loadItemByProductId',
			data : { "id" : id },
			success : function(result) {
				var result = JSON.parse(result);
				for (var i = 0; i < result.length; i++) {
					s += '<option value="' + result[i][0] + '">'+ result[i][1]+ '</option>';
				}
				$('#items').html(s);
			}
		});
	}
	//reset data
	$('#items').html(s);
	$('#units').html(unitTag);
}

function onItemChange(id) {
	var unitTag = '<option selected disabled="disabled" value="">Unit</option>';
	if (id) {
		$.ajax({
			url : '/loadUnitByItemId',
			data : { "id" : id },
			success : function(result) {
				var result = JSON.parse(result);
				for (var i = 0; i < result.length; i++) {
					unitTag += '<option value="' + result[i] + '">'+ result[i]+ '</option>';
				}
				$('#units').html(unitTag);
			}
		});

	}
	//reset data
	$('#units').html(unitTag);
}

//StockOut Validation for quantity
function setMaxQuantity(unit) {
	var quantityTagPrefix = `<input type="number" id="quantity" name="quantity" min="1" `;
	var quantityTagSuffix = `required oninvalid="this.setCustomValidity('Quantity cannot be blank or 0 or more than available stock')" oninput="this.setCustomValidity('')"`
			+ ` onchange="calculateAmount()" >`;
	let itemId = document.getElementsByName("item")[0].value;

	if (unit) {
		$.ajax({
			url : '/loadMaxQuantityByItemIdAndUnit',
			data : { "itemId" : itemId, "unit" :  unit},
			success : function(result) {
				result = `max="` + result + `" ` +  `placeholder="Quantity(` + result + ` available)"`;
				$('#quantity').html(quantityTagPrefix + result + quantityTagSuffix);
			}
		});
	}
	//reset data
	$('#quantity').html(quantityTagPrefix + quantityTagSuffix);
}

function setMaxQuantityForEdit() {
	var stockOutId = document.getElementsByName("id")[0].value;
	let unit = document.getElementsByName("unit")[0].value;

	if(stockOutId == 0) {
		setMaxQuantity(unit);
		
	} else {
		var quantityTagPrefix = `<input type="number" id="quantity" name="quantity" placeholder="Quantity" min="1" `;
		var quantityTagSuffix = `required oninvalid="this.setCustomValidity('Quantity cannot be blank or 0 or more than available stock')" oninput="this.setCustomValidity('')"`
				+ ` onchange="calculateAmount()" >`;
		let itemId = document.getElementsByName("item")[0].value;
		let quantity = document.getElementsByName("quantity")[0].value;

		if (unit) {
			$.ajax({
				url : '/loadMaxQuantityByItemIdAndUnitAndStockOutId',
				data : { "itemId" : itemId, "unit" :  unit, "stockOutId" : stockOutId},
				success : function(result) {
					result = `max="` + result + `" value="` + quantity + `" `;
					$('#quantity').html(quantityTagPrefix + result + quantityTagSuffix);
				}
			});
		}
		$('#quantity').html(quantityTagPrefix + quantityTagSuffix);
	}
}


//StockIn Validation for quantity
function checkIfStockInEditAllowed() {
	let itemId = document.getElementsByName("item")[0].value;
	let unit = document.getElementsByName("unit")[0].value;
	let stockInId = document.getElementsByName("id")[0].value;
	let quantity = document.getElementsByName("quantity")[0].value;

	if (unit) {
		$.ajax({
			url : '/checkIfStockInEditAllowedByItemIdAndUnitAndStockInId',
			data : { "itemId" : itemId, "unit" :  unit, "stockInId" : stockInId, "quantity" : quantity},
			success : function(result) {
				if(result != 'Allowed') {
					alert(quantity + " not allowed to set as quantity since Stock Outs will become more than Stock Ins");
					location.reload(true);
				}
			}
		});
	}
}

function resetQuantity(){
	$("input[name='quantity']").val("");
}

