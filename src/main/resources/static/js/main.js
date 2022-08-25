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

})();

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

function checkIfNumberExist() {
	let number = document.getElementById("phone").value;
	var retVal = true;
	
	$.ajax({
	url : '/checkIfNumberExistsForUser',
	data : { "number" : number },
	async: false,
	success : function(result) {
		if(result == "Exist"){
			alert("Number is already registered with another user!");
			retVal = false;
		}
	}
	});
	
	return retVal;
}


