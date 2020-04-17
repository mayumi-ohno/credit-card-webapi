package com.example.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.CreditCard;
import com.example.domain.OrderNumber;

/**
 * 決済情報確認をするコントローラ.
 * 
 * @author mayumiono
 *
 */
@Controller
@EnableAutoConfiguration
//■ http://niwaka.hateblo.jp/entry/2015/03/31/215844
//■	https://fresopiya.com/2019/09/16/webapijson/
public class WebApiController {

	/**
	 * クレジットカード情報の不正入力チェック
	 * 
	 * @param creditCard クレジットカード情報
	 * @return 確認結果
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> payment(@RequestBody CreditCard creditCard) {
		Map<String, String> result = new HashMap<>();
		Pattern pattern;
		Matcher mather;

		Integer cardExpYear = Integer.parseInt(creditCard.getCard_exp_year());
		Integer cardExpMonth = Integer.parseInt(creditCard.getCard_exp_month());
		Integer lengthOfExpMonth = LocalDate.of(cardExpYear, cardExpMonth, 1).lengthOfMonth();
		LocalDate cardExp = LocalDate.of(cardExpYear, cardExpMonth, lengthOfExpMonth);
		LocalDate today = LocalDate.now();
		Boolean validCardExp = cardExp.compareTo(today) >= 0;
		if (!validCardExp) {
			result.put("status", "error");
			result.put("message", "カードの有効期限が切れています");
			result.put("error_code", "E-01");
			return result;
		}

		pattern = Pattern.compile("^[0-9]{3,4}$");
		mather = pattern.matcher(creditCard.getCard_cvv());
		Boolean validCardCvv = mather.matches();
		if (!validCardCvv) {
			result.put("status", "error");
			result.put("message", "セキュリティコードが不正です(3-4桁の半角数字)");
			result.put("error_code", "E-02");
			return result;
		}

		pattern = Pattern.compile("^[0-9]{4}$");
		mather = pattern.matcher(creditCard.getCard_exp_year());
		Boolean validYear = mather.matches();
		pattern = Pattern.compile("^[0-9]{1,2}$");
		mather = pattern.matcher(creditCard.getCard_exp_month());
		Boolean validMonth = mather.matches();
		if (!validYear || !validMonth) {
			result.put("status", "error");
			result.put("message", "カードの有効期限が不正です");
			result.put("error_code", "E-03");
			return result;
		}

		pattern = Pattern.compile("^[0-9]{16}$");
		mather = pattern.matcher(creditCard.getCard_number());
		Boolean validCardNumber = mather.matches();
		if (!validCardNumber) {
			result.put("status", "error");
			result.put("message", "カード番号が不正です(半角数字16桁)");
			result.put("error_code", "E-04");
			return result;
		}

		pattern = Pattern.compile("^[A-Z]{1,30}$");
		mather = pattern.matcher(creditCard.getCard_name());
		Boolean validCardName = mather.matches();
		if (!validCardName) {
			result.put("status", "error");
			result.put("message", "カード名義人が不正です【1-30字の半角英字(大文字)】");
			result.put("error_code", "E-05");
			return result;
		}

		result.put("status", "success");
		result.put("message", "OK");
		result.put("error_code", "E-00");
		return result;

	}

	/**
	 * 注文キャンセル処理.
	 * 
	 * @param orderNumber 注文番号
	 * @return キャンセルの場合status"success"を返す
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> cancel(@RequestBody OrderNumber orderNumber) {
		Pattern pattern = Pattern.compile("^[0-9]{1,14}$");
		Matcher mather = pattern.matcher(orderNumber.getOrder_number());
		Boolean validOrderNumber = mather.matches();

		Map<String, String> result = new HashMap<>();
		if (!validOrderNumber) {
			result.put("status", "error");
			result.put("message", "注文番号が不正です");
			result.put("error_code", "E-02");
			return result;
		}

		result.put("status", "success");
		result.put("message", "OK");
		result.put("error_code", "E-00");
		return result;
	}
}