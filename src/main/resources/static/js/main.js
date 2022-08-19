(function () {
	"use strict";

	var treeviewMenu = $('.app-menu');

	// Toggle Sidebar
	$('[data-toggle="sidebar"]').click(function(event) {
		event.preventDefault();
		$('.app').toggleClass('sidenav-toggled');
	});

	// Activate sidebar treeview toggle
	$("[data-toggle='treeview']").click(function(event) {
		event.preventDefault();
		if(!$(this).parent().hasClass('is-expanded')) {
			treeviewMenu.find("[data-toggle='treeview']").parent().removeClass('is-expanded');
		}
		$(this).parent().toggleClass('is-expanded');
	});

	// Set initial active toggle
	$("[data-toggle='treeview.'].is-expanded").parent().toggleClass('is-expanded');

	//Activate bootstrip tooltips
	$("[data-toggle='tooltip']").tooltip();

})();


var data = {
			labels: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
			datasets: [
				{
					label: "My First dataset",
					fillColor: "rgba(220,220,220,0.2)",
					strokeColor: "rgba(220,220,220,1)",
					pointColor: "rgba(220,220,220,1)",
					pointStrokeColor: "#fff",
					pointHighlightFill: "#fff",
					pointHighlightStroke: "rgba(220,220,220,1)",
					data: [65, 59, 80, 81, 56]
				},
				{
					label: "My Second dataset",
					fillColor: "rgba(151,187,205,0.2)",
					strokeColor: "rgba(151,187,205,1)",
					pointColor: "rgba(151,187,205,1)",
					pointStrokeColor: "#fff",
					pointHighlightFill: "#fff",
					pointHighlightStroke: "rgba(151,187,205,1)",
					data: [28, 48, 40, 19, 86]
				}
			]
		};




function printData() {
	        var printWindow = window.open("");
	        var html_head = document.getElementById("html_head").outerHTML;
	        printWindow.document.write(html_head);
	 
	        //Print specific CSS.
	        /* var table_style = document.getElementById("table_style").innerHTML;
	        
	        printWindow.document.write('<head><style type = "text/css">');
	        printWindow.document.write(table_style);
	        printWindow.document.write('</style>');
	        printWindow.document.write('</head>');
	        */
	        
	 
	        //Print the DIV contents i.e. the HTML Table.
	        printWindow.document.write('<body onload="window.print()">');
	        
	        printWindow.document.write('<link rel="stylesheet" type="text/css" href="css/print_style.css">');
	        
	        var divContents = document.getElementById("printTable").outerHTML;
	        printWindow.document.write(divContents);
	        
	        printWindow.document.write('</body>');
	 
	        printWindow.document.write('</html>');
	        printWindow.document.close();
	        
	        /*setTimeout(function () {
            printWindow.focus();
            printWindow.print();
             }, 1000); */
             
    }

const ReadAndCompress = (e, clb, cla) => {
			const size = `Before Compression: ${(e.target.files[0].size/(1000*1024)).toFixed(2)} MB`;
			document.querySelector("p[name=before-compression]").innerHTML = size;
			const reader = new FileReader();
			reader.readAsDataURL(e.target.files[0]);
			
			reader.onload = event => {
				const img = document.querySelector("img." + clb);
				img.src = event.target.result;
				// img.style = "display: true";
				img.onload = () => {
					const width = img.width;
					const height = img.height;
					const elem = document.querySelector('canvas');
					elem.width = width;
					elem.height = height;
					const ctx = elem.getContext('2d');
					ctx.drawImage(img, 0, 0, width, height);
					const webp = ctx.canvas.toDataURL("image/webp", 0.5);
					const imgAfter = document.querySelector("img." + cla);
					imgAfter.src = webp;
					imgAfter.style = "display: true";
					const head = 'data:image/webp;base64,';
					const imgFileSize = (Math.round((webp.length - head.length)*3/4) / (1000)).toFixed(2);
					document.querySelector("p[name=after-compression]").innerHTML =
						`After Compression: ${imgFileSize} KB`;
				},
			reader.onerror = error => console.error(error);
			}
		}
		
	document.querySelector("input[name=sPhoto]")
		.addEventListener("change", (event) => ReadAndCompress(event, "before-ph", "after-ph"))
	document.querySelector("input[name=colour]")
		.addEventListener("change", (event) => ReadAndCompress(event, "before-col", "after-col"))

function checkFirstRadioButton() {
	var radio = document.querySelectorAll('.radioBtn'),
    checked = false;
	
	for (var i = 0; i < radio.length; i++) {
    if (radio[i].checked) {
        checked = true;
        break;
		}
	}

	if (!checked) {
		radio[0].checked = true;
	}
}

function calculateAmount() {
	let price = document.getElementsByName("price")[0].value;
	let quantity = document.getElementsByName("quantity")[0].value;
	
	amount = price * quantity;
	
	document.querySelector("input[name=amount]").outerHTML =
						`<input type="number" name="amount" placeholder="Amount" value="` + amount + `" readonly>`;
}
