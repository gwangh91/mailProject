// loginFormScript.js

document.querySelector('form').addEventListener('submit', async function(event) {
	event.preventDefault(); // フォーム提出の基本動作を防止

	let email = document.getElementById('email').value;
	let password = document.getElementById('password').value;
	let errorElement = document.getElementById('error-message');

	if (!email || !password) {
		errorElement.textContent = "メールとパスワードの両方を入力してください。";
		return;
	}

	if (!isValidEmail(email)) {
		errorElement.textContent = "有効なメールの形式を入力してください。";
		return;
	}

	try {

		const response = await fetch('/loging', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ email, password })
		});

		if (!response.ok) {
			throw new Error('Invalid credentials');
		}


		window.location.href = '/mailForm';
	} catch (error) {

		errorElement.textContent = "メールアドレスまたはパスワードが正しくありません。";
	}
});

function isValidEmail(email) {

	let emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
	return emailRegex.test(email);
}