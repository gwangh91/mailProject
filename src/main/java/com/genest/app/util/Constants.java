package com.genest.app.util;

public class Constants {
	public static final String LOGIN_FORM_VIEW = "loginForm";
	public static final String JOIN_FORM_VIEW = "joinForm";
	public static final String MAIL_FORM_VIEW = "mailForm";
	public static final String CONFIRM_MAIL_FORM_VIEW = "confirmMailForm";
	public static final String MAIL_RESULT_VIEW = "mailResult";
	public static final String JOIN_POPUP_VIEW = "joinPopup";

	public static final String SUCCESS_MAIL_MESSAGE = "転送成功";
	
	// メール形式を検証する正規表現式
	public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$";
}