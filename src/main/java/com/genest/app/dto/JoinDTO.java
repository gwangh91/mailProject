package com.genest.app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class JoinDTO {

	// メール
	@NotBlank(message = "メールを入力してください。")
	@Email(message = "メールの形式が正しくありません。")
	private String email;

	// ユーザーパスワード
	@NotBlank(message = "パスワードを入力してください。")
	@Size(min = 5, message = "パスワードは5文字以上入力してください。")
	private String password;

	// ユーザーパスワード確認
	@NotBlank(message = "パスワード確認を入力してください。")
	private String confirmPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}