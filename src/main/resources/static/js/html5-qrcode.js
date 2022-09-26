function scanItemQRCode(entryType) {
	$('#scanModal').modal('show');

	var scanCodeInput = document.getElementsByName('scanCode')[0];
	const html5QrCode = new Html5Qrcode("qr-reader");
	const config = { fps: 10, qrbox: { width: 250, height: 250 } };
	const onScanSuccess = (decodedText, decodedResult) => {
		console.log(`Scan result = ${decodedText}`, decodedResult);
		scanCodeInput.value = decodedText;

		html5QrCode.stop().then((ignore) => {}).catch((err) => {
			console.log(`Error stopping scanner. Reason: ${err}`)
		});

		$('#scanModal').modal('hide');
		if(entryType != null) {
			populateDataIfScanCodeExists(entryType, decodedText);
		}
	}

	html5QrCode.start({ facingMode: "environment" }, config, onScanSuccess);

	const fileinput = document.getElementById('qr-input-file');
	fileinput.addEventListener('change', e => {

		html5QrCode.stop().then((ignore) => {}).catch((err) => {console.log(`Error stopping scanner. Reason: ${err}`)});

		if (e.target.files.length == 0) {
			return;
		}

		const imageFile = e.target.files[0];

		html5QrCode.scanFile(imageFile, true)
		.then(decodedText => {

			scanCodeInput.value = decodedText;
			$('#scanModal').modal('hide');
			if(entryType != null) {
				populateDataIfScanCodeExists(entryType, decodedText);
			}

		})
		.catch(err => {
			$('#scanModal').modal('hide');
			alert(`Error scanning file. Reason: ${err}`);
		});
	});

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



$("#scanModal").on('hidden.bs.modal', function () {
	$(this).data('bs.modal', null);
});

