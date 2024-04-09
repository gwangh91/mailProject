// mailFormScript.js

document.getElementById("emailForm").addEventListener("submit", function(event) {
	// イベントデフォルト動作を停止し、空白をチェック
	event.preventDefault();
	validateForm();
});

function validateForm() {
	// メール取得
	let to = document.getElementById("to").value;
	// タイトル取得
	let subject = document.getElementById("subject").value;
	// 本文取得
	let body = document.getElementById("body").value;

	// メール空白チェック
	if (to === "") {
		alert("メールアドレスを入力してください。");
		document.getElementById("to").focus();
		return;
	}
	// タイトル空白チェック
	if (subject === "") {
		alert("件名を入力してください。");
		document.getElementById("subject").focus();
		return;
	}
	// 本文空白チェック
	if (body === "") {
		alert("本文を入力してください。");
		document.getElementById("body").focus();
		return;
	}
	// 全ての値が空白でなければフォーム提出
	document.getElementById("emailForm").submit();
}
