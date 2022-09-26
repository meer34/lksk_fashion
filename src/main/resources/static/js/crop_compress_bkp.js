/**
 * 
 */

$(document).ready(() => {
	document.querySelector("input[id=itemImage]")
	.addEventListener("change", (event) => readURL(event, "itemImage", "after-ph"));
	document.querySelector("input[id=colourImage]")
	.addEventListener("change", (event) => readURL(event, "colourImage", "after-col"));
});

function readURL(event, newElemName, cla) {
	if (event.target.files[0]) {
		$('#imageUploadModal').modal('show');
		console.log(`Before Compression: ${(event.target.files[0].size/(1000*1024)).toFixed(2)} MB`);

		var reader = new FileReader();
		reader.onload = function (e) {
			$('#blah').attr('src', e.target.result);
		};
		reader.readAsDataURL(event.target.files[0]);

		setTimeout(function() {initCropper(newElemName, cla);}, 100)

	}
}

function initCropper(newElemName, cla){
	
	var $modal = $("#imageUploadModal");
	var crop_image = document.getElementById("blah");
	var cropper;

	$modal.modal('show');

	$modal.on('shown.bs.modal', function(){
		cropper = new Cropper(crop_image, {
			viewMode: 3,
			aspectRatio: 1 / 1,

			cropBoxMovable: true,
			dragMode: 'move',
			autoCropArea: 1,

			toggleDragModeOnDblclick: true,
			zoomOnTouch: true,
			zoomOnWheel: true,

			minContainerWidth: 325,
			minContainerHeight: 325,

			minCanvasWidth : 325,
			minCanvasHeight :325,

			minCropBoxWidth: 150,
			minCropBoxHeight: 150,
			//

			crop: function(e) {
				console.log(e.detail.x);
				console.log(e.detail.y);
			}

		});
	}).on('hidden.bs.modal', function(){
		cropper.destroy();
		crop_image.src = '#';
	});


	//On crop button clicked
	$("#crop_button").click( function(){
		var imgurl =  cropper.getCroppedCanvas().toDataURL("image/jpeg", 0.5);
		
		//base64 code here
		var input = document.createElement("input");
		input.name = newElemName + "Blob";
		input.setAttribute("value", imgurl)
		$(input).insertAfter("#" + newElemName);
		
		const imgAfter = document.querySelector("img." + cla);
		imgAfter.src = imgurl;
		imgAfter.style = "display: true";
		
		const head = 'data:image/webp;base64,';
		const imgFileSize = (Math.round((imgurl.length - head.length)*3/4) / (1000)).toFixed(2);
		console.log(`After crop and compression: ${imgFileSize} KB`);
		
		$('#imageUploadModal').modal('hide');

	});

}


function calculateAmount() {
	let price = document.getElementsByName("price")[0].value;
	let quantity = document.getElementsByName("quantity")[0].value;

	amount = price * quantity;

	document.querySelector("input[name=amount]").outerHTML =
			`<input type="number" name="amount" placeholder="Amount" value="` + amount + `" readonly>`;
}

