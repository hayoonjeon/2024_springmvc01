package com.ict.sns.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.ict.sns.vo.KakaoUserResponse;
import com.ict.sns.vo.KakaoVO;

@RestController
public class KakaoUserInfoController {
	@RequestMapping(value = "/kakaoUserInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String kakaoUserInfo(HttpServletRequest request) {
		// 세션에 저장된 kavo 안에 access_token를 이용해서 사용자 정보 가져오기 
		KakaoVO kavo  = (KakaoVO) request.getSession().getAttribute("kavo");
		String access_token = kavo.getAccess_token();
		
		String apiURL = "https://kapi.kakao.com/v2/user/me" ;
		String header = "Bearer " + access_token;
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", header);
		
		
		return kakaoRequest(apiURL, headers, request);
	}
	
	public String kakaoRequest(String apiURL, Map<String, String> headers, HttpServletRequest request) {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			URL url = new URL(apiURL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			// 헤더 설정 
			for (Entry<String, String> k : headers.entrySet()) {
				conn.setRequestProperty(k.getKey(), k.getValue());
			}
			
			// 응답코드 확인 코드 200이면성공
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				// 토큰 요청 성공 후 결과 받기 (JSON 타입)
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				StringBuffer sb2 = new StringBuffer();
				while((line = br.readLine()) != null) {
					sb2.append(line);
				}
			
				// DB에 저장하기 위한 작업
				// Gson을 사용하여 KakaoUserResponse 객체로 변환
				Gson gson = new Gson();
				KakaoUserResponse kakaoUser = gson.fromJson(sb2.toString(), KakaoUserResponse.class);

				// 필요한 정보 추출
				String nickname = kakaoUser.getProperties().getNickname();
				String profileImage = kakaoUser.getProperties().getProfile_image();
				String id = String.valueOf(kakaoUser.getId());
				String email = kakaoUser.getKakao_account().getEmail(); // 이메일은 선택에 의해 제공이 안 될 수도 있음
				// String fullName = kakaoUser.getKakao_account().getProfile().getNickname();

				//id가지고 사용자 DB에 검색해서 ID가있으면 사용자 정보를 더 가져올 수 있다.
				//id가지고 사용자 DB에 검색해서 ID가없으면 처음 kakao 로그인 한 사람이므로, 등록한다.(insert)
				
				
				// 정보 출력 확인 세션에 저장
				System.out.println("id: " + id);
				System.out.println("nickname: " + nickname);
				System.out.println("profileImage: " + profileImage);
				// System.out.println("fullName: " + fullName);
			 System.out.println("email: " + email);

				
				//세션에 저장
				request.getSession().setAttribute("id", id);
				request.getSession().setAttribute("email", email);
				request.getSession().setAttribute("nickname", nickname);
				request.getSession().setAttribute("profileImage", profileImage);
				// request.getSession().setAttribute("fullName", nickname);


				
				String result = sb2.toString();
				System.out.println(result);
				return result;
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
			  isr.close();
			  br.close();
			  conn.disconnect();
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		
		
		
		return null;
	}
}



