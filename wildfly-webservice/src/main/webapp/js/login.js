$(document).ready(function() {
	$("#login").click(function() {
		var email = $("#email").val();
		var password = $("#password").val();
		if (email == '' || password == '') {
			alert("Fill out form, dude!");
		} else {
			console.log('login');
			$.ajax({
				type : 'POST',
				contentType : 'application/json',
				url : 'http://localhost:8080/wildfly-wenservice/login',
				dataType : "json",
				data : formToJSON(),
				success : function(data, textStatus, jqXHR) {
					alert('Wine created successfully');
					$('#btnDelete').show();
					$('#wineId').val(data.id);
				},
				error : function(jqXHR, textStatus, errorThrown) {
					alert('loginError error: ' + textStatus);
				}

			});
		}

	});
});

function formToJSON() {
	return JSON.stringify({
		"email" : $('#email').val(),
		"password" : $('#password').val(),
	});
}
