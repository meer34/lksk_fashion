function scanItemQRCode(entryType) {
	$('#scanModal').modal('show');
    var resultContainer = document.getElementById('qr-reader-results');
    var lastResult, countResults = 0;
    
    var html5QrcodeScanner = new Html5QrcodeScanner(
        "qr-reader", { fps: 10, qrbox: 250 });
    
    function onScanSuccess(decodedText, decodedResult) {
        if (decodedText !== lastResult) {
            ++countResults;
            lastResult = decodedText;
            console.log(`Scan result = ${decodedText}`, decodedResult);
 
			resultContainer.innerHTML = `<input type="text" name="scanCode" placeholder="Scan Code" value="${decodedText}" autocomplete="off" >`
										+ `<button name="Scan" value="Scan" onClick="scanItemQRCode('Stock Out')">`;
            
            // Optional: To close the QR code scannign after the result is found
            html5QrcodeScanner.clear();
            $('#scanModal').modal('hide');
            if(entryType != null) {
            	populateDataIfScanCodeExists(entryType, decodedText);
            }
			
        }
    }
    
    // Optional callback for error, can be ignored.
    function onScanError(qrCodeError) {
        // This callback would be called in case of qr code scan error or setup error.
        // You can avoid this callback completely, as it can be very verbose in nature.
    }
    
    html5QrcodeScanner.render(onScanSuccess, onScanError);
};

function populateDataIfScanCodeExists(entryType, scanCode){
	var checkCodeUrl, prefetchDataURL;
	if(entryType == "Stock In") {
		checkCodeUrl = '/checkIfScanCodeExistsForStockIn';
		prefetchDataURL = '/addStockInForScanCode?action=Add&scanCode=';
		
	} else if(entryType == "Stock Out") {
		checkCodeUrl = '/checkIfScanCodeExistsForStockOut';
		prefetchDataURL = '/addStockOutForScanCode?action=Add&scanCode=';
	} else return;
	if (scanCode) {
      	$.ajax({
        url : checkCodeUrl,
        data : { "scanCode" : scanCode },
        success : function(result) {
        	if(result == "Exist"){
				window.open(prefetchDataURL + scanCode, '_self');
			}
        }
      });
     }
}


$(document).ready(() => {
    document.getElementById('scanCode').addEventListener('blur', function(event) {
    	console.log('Onblur even occured for scanCode');
    	
    	if(document.getElementsByName("stockInScan").length > 0) {
    		console.log('Populating data for stock in');
    		populateDataIfScanCodeExists('Stock In', event.target.value);
    		
    	} else if(document.getElementsByName("stockOutScan").length > 0) {
    		console.log('Populating data for stock out');
    		populateDataIfScanCodeExists('Stock Out', event.target.value);
    	}
    });
    
    document.getElementById('scanCodeBtn').addEventListener('click', function(event) {
    	console.log('Onclick even occured for scan button');
    	
    	if(document.getElementsByName("stockInScan").length > 0) {
    		console.log('Populating data for stock in');
    		scanItemQRCode('Stock In');
    		
    	} else if(document.getElementsByName("stockOutScan").length > 0) {
    		console.log('Populating data for stock out');
    		scanItemQRCode('Stock Out');
    	}
    });

})



