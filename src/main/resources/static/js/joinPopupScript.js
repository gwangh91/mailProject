// joinPopupScript.js

window.onload = function() {
	let popupContent = '<h2>' + message + '</h2>';

	let popup = window.open('', 'JoinPopup', 'width=400,height=200');

	if (popup && !popup.closed) {
		popup.document.write(popupContent);
		popup.document.write('<button id="confirmButton">確認</button>');

		var confirmButton = popup.document.getElementById('confirmButton');
		confirmButton.onclick = function() {
			popup.close();
			window.location.href = '/login-form';
		};
	} else {
		// ポップアップウィンドウが作成されていない、またはブロックされている場合は、ユーザーにメッセージを表示します。
		alert('ポップアップブロックが検出されました。ポップアップブロックを解除して、もう一度お試しください。');
	}
};