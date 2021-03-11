package com.lovemesomecoding.pizzaria.utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public class ValidationUtils {

	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int PASSWORD_MAX_LENGTH = 150;

	private static String ALL_CHARS_WITH_NO_SPACE = "[\\S]";
	private static String AT_SYMBOL = "@";
	private static String DOT = "[.]";
	private static String LETTERS_AND_NUMBERS = "[\\w-]";

	private static final String EMAIL_PATTERN = ALL_CHARS_WITH_NO_SPACE + "+" + AT_SYMBOL + LETTERS_AND_NUMBERS + "+"
			+ DOT + "+(" + LETTERS_AND_NUMBERS + "+)";

	private static Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

	// ssn
	private static String SSN_REGEX = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";

	/**
	 * Requirements <br>
	 * 1. must have characters with no white space before @ sign <br>
	 * 2. must have @ sign <br>
	 * 3. must have dot after but not directly after @ sign <br>
	 * 4. must have character(s) after dot
	 * 
	 * @param email
	 * @return Boolean
	 */
	public static Boolean isValidEmailFormat(String email) {
		if(email==null || email.length()==0) {
			return false;
		}
		return emailPattern.matcher(email).matches();
	}

	public static List<String> getErrors(BindingResult bindingResult) {
		List<String> errors = bindingResult.getFieldErrors().stream()
				.map(fieldError -> {
					
					String field = fieldError.getField();
					
					String defaultMsg = "";
					
					if(fieldError.getDefaultMessage()!=null) {
						defaultMsg = fieldError.getDefaultMessage();
					}
					
					return field + ", " + defaultMsg;
				})
				.collect(Collectors.toList());
		return errors;
	}

	public static Boolean isPhoneNumberValid(String phoneNumber) {
		if(phoneNumber==null || phoneNumber.length()==0) {
			return false;
		}
		String numbers = StringUtils.getDigits(phoneNumber);
		return (numbers.length() == 10 || numbers.length() == 11) ? true : false;
	}

	/**
	 * String specialChars =
	 * "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
	 * 
	 * @param password
	 * @return
	 */
	public static Boolean isValidPassword(String password) {
		Boolean result = null;
		if (password == null || password.isEmpty()) {
			result = false;
			return result;
		} else {
			StringBuilder patternBuilder = new StringBuilder();
			// lowercase
			patternBuilder.append("((?=.*[a-z])");
			// one number
			patternBuilder.append("(?=.*\\d)");
			// special character
			patternBuilder.append("(?=.*[,~,!,@,#,$,%,^,&,*,(,),-,-,_,=,+,[,{,],},|,;,:,<,>,/,?])");
			// uppercase
			patternBuilder.append("(?=.*[A-Z])");
			// length
			patternBuilder.append(".{" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "})");
			// System.out.println("password: "+password);
			// System.out.println("regex: "+patternBuilder.toString());
			Pattern pattern = Pattern.compile(patternBuilder.toString());
			result = pattern.matcher(password).matches();
			// System.out.println("valid: "+ result);
			return result;
		}
	}

	public static Boolean isValidGender(String gender) {
		if (gender == null) {
			return false;
		}

		if (gender.equals("MALE") || gender.equals("FEMALE") || gender.equals("OTHER")) {
			return true;
		}
		return false;
	}

	public static Boolean isValidMultipartFile(MultipartFile multipartFile) {
		if (multipartFile == null) {
			return false;
		}

		if (multipartFile.isEmpty() == false) {
			return false;
		}

		if (multipartFile.getSize() <= 0) {
			return false;
		}
		return true;
	}

	public static Boolean validateCreditCardNumber(String cardNumber) {
		String numberOnly = cardNumber.trim();
		if (numberOnly.length() != 16) {
			return false;
		}
		int[] ints = new int[numberOnly.length()];
		for (int i = 0; i < numberOnly.length(); i++) {
			ints[i] = Integer.parseInt(numberOnly.substring(i, i + 1));
		}
		for (int i = ints.length - 2; i >= 0; i = i - 2) {
			int j = ints[i];
			j = j * 2;
			if (j > 9) {
				j = j % 10 + 1;
			}
			ints[i] = j;
		}
		int sum = 0;
		for (int i = 0; i < ints.length; i++) {
			sum += ints[i];
		}
		if (sum % 10 == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validateCreditCardPIN(String cardPIN) {
		String numberOnly = cardPIN.trim();
		if (numberOnly.length() != 4) {
			return false;
		}
		try {
			int pin = Integer.parseInt(numberOnly);
			if (pin >= 1000 || pin <= 9999) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isValidSsnFormat(String ssn) {
		if(ssn==null || ssn.length()==0) {
			return false;
		}
		return Pattern.compile(SSN_REGEX).matcher(ssn).matches();
	}

	public static boolean isValidUrl(String url) {
		if(url==null || url.length()==0) {
			return false;
		}
		boolean valid = false;

		if (url != null && url.isEmpty()==false) {
			try {
				new URL(url).toURI();
				valid = true;
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return valid;
	}

}
