
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
      var s = '<option selected disabled="disabled" value="">Unit</option>';
      if (id) {
      	$.ajax({
        url : '/loadUnitByItemId',
        data : { "id" : id },
        success : function(result) {
        	var result = JSON.parse(result);
        	for (var i = 0; i < result.length; i++) {
        	  s += '<option value="' + result[i] + '">'+ result[i]+ '</option>';
        	}
        	$('#units').html(s);
        }
      });
     }
     //reset data
     $('#units').html(s);
}
