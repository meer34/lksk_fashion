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

		setTimeout(function() {initCropper(newElemName, clb, cla);}, 300)

	}
}

function initCropper(newElemName, clb, cla){
	var image = document.getElementById('blah');
	
	//Fix orientation
	try {
		fixExifOrientation(image);
	} catch(err) {
		  console.log('Issue in exif rotation fix!');
	}
	
	var width = image.clientWidth;
	var height = image.clientHeight;
	
	if (height > width) {
		image.style.width = 'auto';
	    image.style.height = '325px';
	    
	} else if(width > height) {
		image.style.width = '325px';
	    image.style.height = 'auto';
	    
	} else if(width == 0 && height == 0) {
		setTimeout(function() {
			image = document.getElementById('blah');
			width = image.clientWidth;
			height = image.clientHeight;
		}, 500);
		if (height > width) {
			image.style.width = 'auto';
		    image.style.height = '325px';
		    
		} else if(width > height) {
			image.style.width = '325px';
		    image.style.height = 'auto';
		    
		} else if(width == 0 && height == 0) {
			image.style.width = '325px';
		    image.style.height = 'auto';
		}
		
	}
	
	var cropper = new Cropper(image, {
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

		crop: function(e) {
			console.log(e.detail.x);
			console.log(e.detail.y);
		}

	});

	// On crop button clicked
	document.getElementById('crop_button').addEventListener('click', function(){
		var imgurl =  cropper.getCroppedCanvas().toDataURL("image/jpeg", 0.1);

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

		$('#blah').attr('src', '#')
		$('#imageUploadModal').modal('hide');
		cropper.destroy();

	});

	$("#imageUploadModal").on("hide.bs.modal", function(){
		console.log('Destroying cropper');
		$('#blah').attr('src', '#');
		cropper.destroy();
	});
}

function fixExifOrientation(image) {
        EXIF.getData(image[0], function() {
            console.log('Exif=', EXIF.getTag(this, "Orientation"));
            
            const canvas = document.createElement("canvas");
            const context = canvas.getContext("2d");

            let { width, height } = image;

            const [outputWidth, outputHeight] = EXIF.getTag(this, "Orientation") >= 5 && EXIF.getTag(this, "Orientation") <= 8
                ? [height, width]
                : [width, height];

            const scale = outputWidth > maxWidth ? maxWidth / outputWidth : 1;

            width = width * scale;
            height = height * scale;

            // set proper canvas dimensions before transform & export
            canvas.width = outputWidth * scale;
            canvas.height = outputHeight * scale;
            
            switch(parseInt(EXIF.getTag(this, "Orientation"))) {
            	case 2: context.transform(-1, 0, 0, 1, width, 0); break;
            	case 3: context.transform(-1, 0, 0, -1, width, height); break;
            	case 4: context.transform(1, 0, 0, -1, 0, height); break;
            	case 5: context.transform(0, 1, 1, 0, 0, 0); break;
            	case 6: context.transform(0, 1, -1, 0, height, 0); break;
            	case 7: context.transform(0, -1, -1, 0, height, width); break;
            	case 8: context.transform(0, -1, 1, 0, 0, width); break;
            	default: break;
            }
            context.drawImage(image, 0, 0, width, height);

            // export base64
            const srcEncoded = canvas.toDataURL("image/jpeg")
            image.src = srcEncoded;
    });
}


function calculateAmount() {
	let price = document.getElementsByName("price")[0].value;
	let quantity = document.getElementsByName("quantity")[0].value;

	amount = price * quantity;

	document.querySelector("input[name=amount]").outerHTML =
			`<input type="number" name="amount" placeholder="Amount" value="` + amount + `" readonly>`;
}

