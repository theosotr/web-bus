/**
 * Takes user's input and if they are
 * validated, it sends them to the
 * CheckInputServlet for editing.
 */
function checkInputIfExists(input) {

	if(acceptInput(input)) {
		$.ajax({
			url: "checkInput",
			data: {
				value: $('#' + input).val(),
				inputType: input
			},
			success: function(data) {
				$('#accept' + input).text(data);
			},
			error: function() {
				alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
			}

		});
	}

}

/**
 * Takes user's input and validates them
 */
function acceptInput(input) {
	var accepted = true;
	if(input == "username") {
		if($("#" + input).val().length < 3) {
			$("#accept" + input).text("Το username σας πρέπει να αποτελείται" +
			" από τουλάχιστον 3 χαρακτήρες");
			accepted = false;
		}
	} else {
		var words = $("#" + input).val().split("@");
		if(words.length == 1 || words.length > 2) {
			$('#accept' + input).text("Η τιμή που δώσατε δεν είναι αποδεκτή");
			accepted = false;
		} else {
			var words2 = words[1].split(".");
			if(words2.length == 1 || words2.length > 2) {
				$('#accept' + input).text("Η τιμή που δώσατε δεν είναι αποδεκτή");
				accepted = false;
			}
		}
	}

	return accepted;
}

/**
 * Checks if the two passwords are same and have more
 * than 6 characters. If not it doesn't accept them.
 */
function verifyPasswords() {
	var password1 = $('#pswrd1').val();
	var password2 = $('#pswrd2').val();
	var accepted = false;
	if(password1 == password2)
		if(password1.length >= 6) {
			$('#acceptPswrd').text("Οι κωδικοί ταιριάζουν");
			accepted = true;
		} else
			$('#acceptPswrd').text("Ο κωδικός θα πρέπει να περιέχει τουλά" +
			"χιστον 6 χαρακτήρες");
	else
	if(password1 == "")
		$('#acceptPswrd').text("Δώστε έναν κωδικό" +
		"Το πεδίο είναι υποχρεωτικό");
	else
		$('#acceptPswrd').text("Οι κωδικοί που δώσατε δεν ταιριάζουν");
	return accepted;
}

/**
 *  Takes user's credentials it validates them
 *  and if they are, it sends them to the User class
 *  for creating a new user.
 */
function createAccount() {
	var firstName = $('#firstName').val();
	var surname = $('#surname').val();
	checkInputIfExists('username');
	checkInputIfExists('email');
	var usernameAccepted = $('#acceptusername').text();
	var emailAccepted = $('#acceptemail').text();
	var passwordAccepted = verifyPasswords();
	if(firstName == "" || surname == ""
		|| usernameAccepted != "Το username σας είναι αποδεκτό"
		|| emailAccepted != "Το email σας είναι αποδεκτό"
		|| !passwordAccepted) {

		alert("Η εγγραφή δεν μπόρεσε να πραγματοποιηθεί παρακαλώ" +
		" ελέξτε ξανά τα πεδία");
	} else {
		$.ajax({
			url: "addUser",
			data: {
				name: firstName,
				lastName: surname,
				username: $('#username').val(),
				email: $('#email').val(),
				password: $('#pswrd1').val()
			},
			success: function(data) {
				alert(data);
				window.location.reload(true);
			},
			error: function() {
				alert("Κάτι πήγε στραβά παρακαλώ δοκιμάστε ξανά");
			}

		});
	}

}
