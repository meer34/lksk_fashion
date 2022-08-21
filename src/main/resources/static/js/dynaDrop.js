
function onProductChange(id) {
      var s = '<option selected disabled="disabled" value="">Item</option>';
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

function setMaxQuantity(id) {
    var quantityTagPrefix = `<input type="number" id="quantity" name="quantity" placeholder="Quantity" min="1" `;
    var quantityTagSuffix = `required oninvalid="this.setCustomValidity('Quantity cannot be blank or 0 or more than available stock')" oninput="this.setCustomValidity('')"`
  		  					+ ` onchange="calculateAmount()" >`;
    if (id) {
    $.ajax({
         url : '/loadAvailableStockByItemId',
         data : { "id" : id },
         success : function(result) {
         	result = `max="` + result + `"`;
         	$('#quantity').html(quantityTagPrefix + result + quantityTagSuffix);
         }
       });
   }
    
}
