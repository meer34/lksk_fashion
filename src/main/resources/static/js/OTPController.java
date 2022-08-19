package com.lksk.web.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lksk.web.model.User;
import com.lksk.web.repo.UserRepository;

@Controller
@PropertySource("classpath:lksk.properties")
public class OTPController {

	@Value("${fast2sms.api.url}")
	private String apiURL;

	@Value("${fast2sms.api.key}")
	private String apiKey;

	@Value("${fast2sms.api.request.contentType}")
	private String contentType;

	@Autowired UserRepository userRepo;
	
	@GetMapping("/send-login-otp")
	@ResponseBody 
	public String showCreateItemPage(HttpServletResponse response, @RequestParam String phone) {
		String responseText = "Request Failed";
		try {
			String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
			System.out.println("Generated OTP - " + otp);

			User user = userRepo.getUserByPhoneNumber(phone);
			if(phone.equals(user.getPhone())) {
				user.setOtp(new BCryptPasswordEncoder().encode(otp));
				userRepo.save(user);
			} else {
				return responseText;
			}

			String requestBody = new StringBuilder("sender_id=")
					.append("FTWSMS")
					.append("&message=")
					.append("Your login OTP is - ")
					.append(otp)
					.append("&route=v3&numbers=")
					.append(phone)
					.append("&flash=")
					.append("0")
					.toString();
			System.out.println("requestBody is " + requestBody);
			responseText = sendPOSTRequest(apiURL, requestBody);
			System.out.println("responseText is " + responseText);

			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return responseText;
	}

	private String sendPOSTRequest(String url, String requestBody) throws Exception {
		StringBuffer response = new StringBuffer();
		System.out.println("Inside sendPOSTRequest method for URL:" + url);
		URL urlObj = new URL(url);
		System.out.println("URL is " + urlObj.getPath());
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		System.out.println("Cnnection opened");
		con.setRequestMethod("POST");
		con.setRequestProperty("authorization", apiKey);
		con.setRequestProperty("Content-Type", contentType);

		System.out.println("Property set done");

		con.setDoOutput(true);

		try(OutputStream os = con.getOutputStream()) {
			byte[] input = requestBody.getBytes("utf-8");
			System.out.println("Input is: " + input.toString());
			os.write(input, 0, input.length);
			System.out.println("Input length is: " + input.length);
		}

		System.out.println("No issues yet");
		int responseCode = con.getResponseCode();
		System.out.println("Response code is: " + responseCode);

		System.out.println("Received Response Code for Fast2SMS Server :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response.toString());
		} else {
			System.out.println("OTP request did not work!");
		}

		return response.toString();

	}

}
