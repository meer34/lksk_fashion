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
	        var printWindow = window.open("Print");
	        
	        printWindow.document.write('<head>')
	        printWindow.document.write('<meta name="description" content="">');
	        printWindow.document.write('<title>LKSK FASHION</title>');
	        printWindow.document.write('<meta charset="utf-8">');
	        printWindow.document.write('<meta http-equiv="X-UA-Compatible" content="IE=edge">');
	        printWindow.document.write('<meta name="viewport" content="width=device-width, initial-scale=1">');
	        printWindow.document.write('<link rel="shortcut icon" type="image/icon" href="images/favicon.png" />');
	        printWindow.document.write('<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">')
	        printWindow.document.write('<link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">')
	    	printWindow.document.write('<link rel="stylesheet" type="text/css" href="css/print_style.css">')
	        printWindow.document.write('</head>');
	        printWindow.document.write('<body onload="window.print()">');
	        
	        var divContents = document.getElementById("printTable").outerHTML;
	        
	        printWindow.document.write(divContents);
	        printWindow.document.write('</body>');
	        printWindow.document.write('</html>');
	        printWindow.document.close();
    }

function checkIfNumberExistForOthers(userType) {
	let phone = document.getElementById("phone").value;
	let userId = document.getElementById("userId").value;
	
	var retVal = true;
	var url;
	
	if(userType == 'ADMIN') {
		url = '/checkIfNumberExistsForOtherAdmins';
	} else if(userType == 'MODERATOR') {
		url = '/checkIfNumberExistsForOtherModerators';
	}
	
	$.ajax({
	url : url,
	data : { "phone" : phone, "id" : userId },
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


