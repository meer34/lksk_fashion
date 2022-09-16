package com.lksk.web.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
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
	
	@GetMapping("/generatePin")
	@ResponseBody 
	public String showCreateItemPage(HttpServletResponse response, @RequestParam String phone) {
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			
			String otp= new DecimalFormat("0000").format(new Random().nextInt(9999));
			
			User user = userRepo.getUserByPhoneNumber(phone);
			if(user != null && phone.equals(user.getPhone())) {
				user.setPin(new BCryptPasswordEncoder().encode(otp));
				user.setPinGenerationTime(new Date());
				userRepo.save(user);
				
			} else {
				System.out.println("User Not Found with phone number:" + phone);
				return "User Not Found";
			}

			String requestBody = new StringBuilder("sender_id=")
					.append("FTWSMS")
					.append("&message=")
					.append("Hi " + user.getUsername() + ",\nYour login OTP is - ")
					.append(otp)
					.append("&route=v3&numbers=")
					.append(phone)
					.append("&flash=")
					.append("0")
					.toString();
			
			return sendPOSTRequest(apiURL, requestBody);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Request Failed";
		}

	}

	private String sendPOSTRequest(String url, String requestBody) throws Exception {
		
		URL urlObj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("authorization", apiKey);
		con.setRequestProperty("Content-Type", contentType);
		con.setDoOutput(true);

		try(OutputStream os = con.getOutputStream()) {
			byte[] input = requestBody.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		int responseCode = con.getResponseCode();

		System.out.println("Received Response Code for Fast2SMS Server :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			return "Success";
		} else {
			System.out.println("OTP request did not work!");
			return "Request Failed";
		}

	}

}
