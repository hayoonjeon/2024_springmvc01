package com.ict.sns.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.ict.sns.vo.AddrVO;
import com.ict.sns.vo.KakaoVO;
import com.ict.sns.vo.NaverVO;

@Controller
public class SnSLoginController {

	@GetMapping("/sns_login")
	public ModelAndView getSpringSnsGo() {
		return new ModelAndView("sns/loginForm");
	}

	@RequestMapping("/kakaologin")
	public ModelAndView kakaologin(HttpServletRequest request) {
		// 1.인증코드받기
		String code = request.getParameter("code");
		System.out.println("code :" + code);

		// 2. 토큰요청(인증코드 필요)
		String reqURL = "https://kauth.kakao.com/oauth/token";

		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// POST 요청
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// 헤더요청
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

			// 데이터 전송
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuffer sb = new StringBuffer();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=fc0f2a1e052696d852c1539b4d217b28");
			sb.append("&redirect_uri=http://localhost:8080/kakaologin");
			sb.append("&code=" + code);
			sb.append("&client_secret=ZK6DTMV2noSndLUBECQhNdVxhLAeApuC");
			bw.write(sb.toString());
			bw.flush();

			// 결과코드가 200이면성공
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 토큰 요청 성공 후 결과받기(JSON타입)
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				StringBuffer sb2 = new StringBuffer();
				while ((line = br.readLine()) != null) {
					sb2.append(line);

				}
				String result = sb2.toString();
				System.out.println(result);

				// 가져온 정보를 바탕으로 DB에 넣거나 session에 저장
				Gson gson = new Gson();
				KakaoVO kavo = gson.fromJson(result, KakaoVO.class);
				
				
				System.out.println(kavo.getAccess_token());
				System.out.println(kavo.getRefresh_token());
				System.out.println(kavo.getScope());
				System.out.println(kavo.getToken_type());
				
				
				request.getSession().setAttribute("kavo", kavo);
			
				
				
				return new ModelAndView("sns/result01");

			} else {
				// 오류 메시지 출력
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				String line = "";
				StringBuffer sbError = new StringBuffer();
				while ((line = br.readLine()) != null) {
					sbError.append(line);
				}
				System.out.println("Error response: " + sbError.toString());
				return new ModelAndView("sns/error");
			}

		} catch (Exception e) {
			System.out.println(e);
			return new ModelAndView("sns/error");
		}

	
	

	}
	@GetMapping("/kakaologout")
	public ModelAndView kakaoLogout(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ModelAndView("sns/loginForm");
		
	}
	@GetMapping("/naverlogin")
	public ModelAndView naverlogin(HttpServletRequest request) {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
	      // 2. 토큰 받기(인가코드 필요)
	      String reqURL = "https://nid.naver.com/oauth2.0/token";
	      try {
	           URL url = new URL(reqURL);
	           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	           
	          
	           //POST 요청
	           conn.setRequestMethod("POST");
	           conn.setDoOutput(true);
	           
	           //헤더 요청
	           conn.setRequestProperty("Content-type","application/x-www-form-urlencoded;charset=utf-8");
	           
	           //본문 
	           BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	           
	           StringBuffer sb = new StringBuffer();
	           sb.append("grant_type=authorization_code");
	           sb.append("&client_id=pkOdTrbseb_iSEoDm8uF");
	           sb.append("&client_secret=XHB8SsXjp8");
	           sb.append("&code="+code);
	           sb.append("&state="+state);
	           
	           bw.write(sb.toString());
	           bw.flush();
	           
	           // 200이면 성공
	           int responseCode = conn.getResponseCode();
	           System.out.println("responseCode ;" + responseCode);
	           
	           if(responseCode == HttpURLConnection.HTTP_OK) {
	              // 토큰 요청 성공 후 결과 받기 (JSON 타입)
	              BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	              String line = "";
	              StringBuffer sb2 = new StringBuffer();
	              while ( ( line = br.readLine()) != null) {
	                 sb2.append(line);
	              }
	              String result = sb2.toString();
	              System.out.println(result);
	              
	              Gson gson = new Gson();
					NaverVO navo = gson.fromJson(result, NaverVO.class);
					
					
					System.out.println(navo.getAccess_token());
					System.out.println(navo.getRefresh_token());
					System.out.println(navo.getToken_type());
					
					//해당정보를 ajax컨트롤러에서사용하기위해 세셔에저장
					request.getSession().setAttribute("navo", navo);

	              
	              return new ModelAndView("sns/result02");
	              
	           }else {
	            // 오류 메시지 출력
	                   BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	                   String line = "";
	                   StringBuffer sbError = new StringBuffer();
	                   while ((line = br.readLine()) != null) {
	                       sbError.append(line);
	                   }
	                   System.out.println("Error response: " + sbError.toString());
	                   return new ModelAndView("sns/error");
	   		}

			} catch (Exception e) {
				System.out.println(e);
				return new ModelAndView("sns/error");
			}
	}
	@GetMapping("/naverlogout")
	public ModelAndView naverLogout(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ModelAndView("sns/loginForm");
		
	}
	@GetMapping("/daum_addr")
	public ModelAndView daumAddr() {
		return new ModelAndView("sns/loginForm");
	}
	
	@GetMapping("/daum_addr_Form")
	public ModelAndView daumAddrForm() {
		return new ModelAndView("sns/addrForm");
	}
	
	@PostMapping("/daum_addr_ok")
	public ModelAndView daumAddrOK(AddrVO addrvo, HttpSession session) {
		
		// 로그인 성공한 상태에서 session 안에 있는 사용자 정보 중 m_id 나 사용자테이블의 pk를 호출한다. 
		// String m_id = (String) session.getAttribute("m_id") ;
		// addrvo.setM_id(m_id);
		
		System.out.println(addrvo.getPostcode());
		System.out.println(addrvo.getAddress());
		System.out.println(addrvo.getDetailAddress());
		System.out.println(addrvo.getExtraAddress());
		
		// 나중에는 DB에 저장하는 코드가 나와야 한다.
		
		return new ModelAndView("sns/loginForm");
	}
	
	@GetMapping("/kakao_map")
	public ModelAndView kakaoMap() {
		return new ModelAndView("sns/loginForm");
	}
	
}








