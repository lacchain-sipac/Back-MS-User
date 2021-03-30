package com.invest.honduras.util;

public class Constant {

	public static final int STATUS_OK = 00000;
	public static final int STATUS_FAIL = 500;

	public static final String TYPE_NOTIFY_USER_CREATED = "1";
	public static final String HOST_NOTIFY = System.getenv("MS-NOTIFY");
    public static final String HOST_GATEWAY = System.getenv("MS-API-GATEWAY");

	
	public static final String API_URL_USERMAIL = "api/v1/notify/send-mail";
    public static final String API_URL_REFRESH_TOKEN= "api/v1/auth/refresh-token";	
	public static final String API_URL_COMPLETE_PERFIL = "api/v1/notify/send-notify";
	public static final String CODE_ROLE_ADMIN = "ROLE_1";

	public static final String MESSAGE_USER_EMAIL_EXIST = "Email ya existe";
	public static final String MESSAGE_USER_NOT_EXIST = "Usuario no existe";

	public static final String TYPE_COMPLETE_PASSWORD = "COMPLETE_PASSWORD";
	public static final String TYPE_INVITATION_USER = "INVITATION_USER";
	public static final String TYPE_UPDATE_USER = "UPDATE_USER";
	public static final String TYPE_UPDATE_STATE_USER = "UPDATE_STATE_USER";
	public static final String TYPE_FORGET_PASSWORD = "FORGET_PASSWORD";
	public static final String TYPE_CHANGE_PASSWORD = "CHANGE_PASSWORD";

	public static final String MESSAGE_REFRESH_TOKEN_BAD_REQUEST = "BAD REQUEST REFRESH TOKEN";
	public static final String MESSAGE_REFRESH_TOKEN_UNAUTHORIZED =   "UNAUTHORIZED REFRESH TOKEN";
	public static final String MESSAGE_REFRESH_TOKEN_ERROR = "Internal Error";	
	
	public static final String TYPE_PROFILE = "TYPE_PROFILE";
	public static final String TYPE_FIND_USER = "TYPE_FIND_USER";

	public static final String USER_POR_COMPLETAR = "P";
	public static final String USER_HABILITADO = "H";

	public static final String REGEX = "^(?=.*\\d)(?=.*[\\u0021-\\u002b\\u003c-\\u0040])(?=.*[A-Z])(?=.*[a-z])\\S{8,12}$";

	public static final String NOTIFY_DID_USER_ATTACH = "Se registró su cuenta exitosamente.";

	public static final String TYPE_TEMPLATE_GENERAL = "general";
	public static final String TYPE_NOTFIY_GENERAL = "general";
	public static final String NOTIFY_SUBJET = "Proceso Fiduciario Invest-H";

	public static final String API_URL_BLOCKCHAIN_USER = "/api/v1/blockchain/user";
	public static final String API_URL_BLOCKCHAIN_USER_CAP = "/api/v1/blockchain/user-cap";
	public static final String HOST_BLOCKCHAIN = System.getenv("MS-BLOCKCHAIN");
	
	public static final String SESSION = "Session_";
	public static  final String  USER_CODE_DISABLED = "D";
	
	public static  final String  USER_CODE_STATUS_ENABLED = "H";
	

}
