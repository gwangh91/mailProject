// joinFormScript.js

$(document).ready(function() {
	// メール形式、重複確認
	$('#email').blur(function() {
		let email = $(this).val();

		if (email.trim() === '') {
			$('#emailError').text('メールを入力してください。').css('color', 'red');
			return;
		}
		$.ajax({
			type: 'GET',
			url: '/join-check-email',
			data: { email: email },
			success: function(response) {
				if (response.formatError) {
					$('#emailError').text('メールの形式が正しくありません。').css('color', 'red');
				} else if (response.exists) {
					$('#emailError').text('存在しているメールです。 別のメールの入力してください。').css('color', 'red');
				} else {
					$('#emailError').text('使用可能なメールです。').css('color', '#B0F6AC');
					enableSubmitButton();
				}
			}
		});
	});

	// パスワードの確認が一致しているか確認
	$('#password').blur(function() {
		let password = $('#password').val();

		if (password.trim() === '') {
			$('#passwordError').text('パスワードを入力してください。').css('color', 'red');
			return;
		}
		if (password.length < 5) {
			$('#passwordError').text('パスワードは5文字以上入力してください。').css('color', 'red');
		} else {
			$('#passwordError').text('使用可能なパスワードです。').css('color', '#B0F6AC');
			enableSubmitButton();
		}
	});

	// パスワードとパスワードの確認が一致しているか確認
	$('#confirmPassword').blur(function() {
		let password = $('#password').val();
		let confirmPassword = $(this).val();
		if (confirmPassword.trim() === '') {
			$('#confirmPasswordError').text('パスワード確認を入力してください。').css('color', 'red');
			return;
		}
		if (password !== confirmPassword) {
			$('#confirmPasswordError').text('パスワードが一致しません。').css('color', 'red');
		} else {
			$('#confirmPasswordError').text('パスワードが一致します。').css('color', '#B0F6AC');
			enableSubmitButton();
		}
	});

	// 登録ボタン活性化
	function enableSubmitButton() {
		console.log("enableSubmitButton");
		let emailValid = ($('#emailError').text() === '使用可能なメールです。');
		let passwordMatch = ($('#passwordError').text() === '使用可能なパスワードです。');
		let confirmPasswordMatch = ($('#confirmPasswordError').text() === 'パスワードが一致します。');
		if (emailValid && passwordMatch && confirmPasswordMatch) {
			$('#submitBtn').prop('disabled', false);
		}
	}
});