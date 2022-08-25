/**
 * 
 */

$(document).ready(() => {
	document.querySelector("input[id=itemImage]")
		.addEventListener("change", (event) => readURL(event, "itemImage", "before-ph", "after-ph"));
	document.querySelector("input[id=colourImage]")
		.addEventListener("change", (event) => readURL(event, "colourImage", "before-col", "after-col"));
})

function readURL(event, newElemName, clb, cla) {
	if (event.target.files[0]) {
		$('#imageUploadModal').modal('show');
		console.log(`Before Compression: ${(event.target.files[0].size/(1000*1024)).toFixed(2)} MB`);
		
		var reader = new FileReader();
		reader.onload = function (e) {
			$('#blah').attr('src', e.target.result);
		};
		reader.readAsDataURL(event.target.files[0]);
		
		setTimeout(function() {initCropper(newElemName, clb, cla);}, 100)
	
	}
}

function initCropper(newElemName, clb, cla){
	var image = document.getElementById('blah');
	var cropper = new Cropper(image, {
		aspectRatio: 1 / 1,
		
		//
		cropBoxMovable: true,
        dragMode: 'move',
        left: 0,
        top: 0,

        toggleDragModeOnDblclick: true,
        zoomOnTouch: true,
        zoomOnWheel: true,

        minContainerWidth: 400,
        minContainerHeight: 400,

        minCanvasWidth : 400,
        minCanvasHeight :400,

        minCropBoxWidth: 150,
        minCropBoxHeight: 150,
		//
		
		crop: function(e) {
			console.log(e.detail.x);
			console.log(e.detail.y);
		}
	});

// On crop button clicked
document.getElementById('crop_button').addEventListener('click', function(){
	var imgurl =  cropper.getCroppedCanvas().toDataURL();
	var img = document.createElement("img");
	img.src = imgurl;
	img.onload = () => {
		const width = img.width;
		const height = img.height;
		const elem = document.querySelector('canvas');
		elem.width = width;
		elem.height = height;
		const ctx = elem.getContext('2d');
		ctx.drawImage(img, 0, 0, width, height);
		const webp = ctx.canvas.toDataURL("image/jpeg", 0.15);
			//base64 code here
			console.log("webp data is: " + webp);
			var input = document.createElement("input");
			input.name = newElemName + "Blob";
			input.setAttribute("value", webp)
			$(input).insertAfter("#" + newElemName);
		
		const imgAfter = document.querySelector("img." + cla);
		imgAfter.src = webp;
		imgAfter.style = "display: true";
		
		const head = 'data:image/webp;base64,';
		const imgFileSize = (Math.round((webp.length - head.length)*3/4) / (1000)).toFixed(2);
		console.log("After Compression: ${imgFileSize} KB");
		
		$('#blah').attr('src', '#')
		$('#imageUploadModal').modal('hide');
		cropper.destroy();
		image.data('cropper', null);
	}

});

$("#imageUploadModal").on("hide.bs.modal", function(){
    console.log('Destroying cropper');
	$('#blah').attr('src', '#');
	cropper.destroy();
});
}


function calculateAmount() {
	let price = document.getElementsByName("price")[0].value;
	let quantity = document.getElementsByName("quantity")[0].value;
	
	amount = price * quantity;
	
	document.querySelector("input[name=amount]").outerHTML =
						`<input type="number" name="amount" placeholder="Amount" value="` + amount + `" readonly>`;
}

