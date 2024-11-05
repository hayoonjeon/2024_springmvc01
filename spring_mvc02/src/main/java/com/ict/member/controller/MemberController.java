package com.ict.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ict.member.service.MemberService;
import com.ict.member.vo.MemberVO;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/member_login")
	public ModelAndView Login(MemberVO mvo, HttpSession session) {
		try {
			ModelAndView mv = new ModelAndView("");
			MemberVO mvo2 = memberService.getMemberLogin(mvo.getM_id());
			
	
			
			if (mvo2 == null) {
			//alert 가능함. 로그인실패시 .
				return new ModelAndView("/sns/login_error");
			}else {
				 //비밀번호 검사
				if(passwordEncoder.matches(mvo.getM_pw(), mvo2.getM_pw())) {
					//성공
					session.setAttribute("loginchk", "ok");
					//일반유저와 관리자 유저 구분
					if (mvo2.getM_id().equals("admin")) {
						session.setAttribute("admin", "ok");
					}
					mv.setViewName("redirect:/shop");
					session.setAttribute("mvo2", mvo2);
					return mv;
				}else {
					//비밀번호 안맞아서 로그인실패
					//request에 값저장해서 loginForm에서 로그인실패 alert 할수있음.
					return new ModelAndView("/sns/login_error");
				}
			}
			
		
		} catch (Exception e) {
			System.out.println(e);
		}
		

		return null;
	}
	@GetMapping("/member_logout")
	public ModelAndView getMemberLogOut(HttpSession session) {
//		//세션초기화(전체삭제)
		session.invalidate();
		
		//필요한 정보만 삭제
		//session.removeAttribute("loginchk");
		//session.removeAttribute("admin");
		//session.removeAttribute("mvo2");
		
		return new ModelAndView("redirect:/shop");
	}
}
