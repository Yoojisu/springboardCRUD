package cafe.ebsteam.springboard.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class LoginController {
	@GetMapping("login")
	public String login() {
		return "login";
	}

	@PostMapping("login")
	public String login(HttpSession session,
			@RequestParam("id") String id, @RequestParam("pw") String pw) {
		final String dbId = "admin";
		final String dbPw = "1234";

		if (id.equals(dbId) && pw.equals(dbPw)) {
			// 로그인 성공->index로 이동-> 로그인 정보 세션 등록->...
			session.setAttribute("id", id);

			return "redirect:/index";
		} else {
			// 로그인 실패->login폼으로 리다이렉트
			return "redirect:/login";
		}
	}
}
