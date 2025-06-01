package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.LoginDto;
import ssd.springcooler.gachiwatch.dto.MemberRegisterDto;
import ssd.springcooler.gachiwatch.service.MemberService;

import java.util.Arrays;

@Controller
@RequestMapping("/member")
public class AccountController {

    private final MemberService memberService;

    @Autowired
    public AccountController(MemberService memberService) {
        this.memberService = memberService;
    }

//    // 회원 가입
//    @PostMapping("/register")
//    public String register(MemberRegisterDto regiDto, Model model) {
//        memberService.register(regiDto);
//        model.addAttribute("result", "success");
//        return "/member/registerResult";
//    }
//
//    //signup 시 email 중복확인 버튼 구현
//    @RequestMapping("emailCheck")
//    @ResponseBody // ajax 요청에 담긴 값을 자바 객체로 변환시켜 인스턴스(boolean)에 저장 -> illegalargumentException 방지
//    public boolean emailCheck(@RequestParam("email") String email) {
//        //DB 들어가서 email 중복값이 있나 들고나옴
//        String check = null;
//        check =MemberService.emailCheck(email); //input email에서 받은 email을 DB까지 전달하여 조회작업
//        if(check != null) return true;
//        else return false;
//    }


        /** ✅ STEP1: 기본 정보 입력 화면 */
        @GetMapping("/register/step1")
        public String registerStep1(Model model) {
            model.addAttribute("member", new MemberRegisterDto());
            return "member/signup-step1"; // Thymeleaf 템플릿
        }

        /** ✅ STEP1 폼 전송 → STEP2로 이동 */
        @PostMapping("/register/step2")
        public String registerStep2(@ModelAttribute("member") MemberRegisterDto dto, HttpSession session) {
            session.setAttribute("tempMember", dto); // 임시 저장
            return "member/signup-step2"; // step2 화면으로 이동
        }

        /** ✅ STEP2에서 닉네임, ott, 장르 입력 받아 최종 회원가입 */
        @PostMapping("/register")
        public String registerFinal(@RequestParam String nickname,
                                    @RequestParam String subscribedOtts,
                                    @RequestParam String preferredGenres,
                                    HttpSession session,
                                    Model model) {
            MemberRegisterDto dto = (MemberRegisterDto) session.getAttribute("tempMember");
            if (dto == null) {
                return "redirect:/member/register/step1";
            }

            dto.setNickname(nickname);
            dto.setSubscribedOtts(Arrays.stream(subscribedOtts.split(","))
                    .map(String::trim)
                    .map(Platform::valueOf)
                    .toList());

            dto.setPreferredGenres(Arrays.stream(preferredGenres.split(","))
                    .map(String::trim)
                    .map(Genre::valueOf)
                    .toList());

            memberService.register(dto);
            session.removeAttribute("tempMember");

            model.addAttribute("result", "success");
            return "member/registerResult";
        }

        /** ✅ 이메일 중복 확인 (AJAX 요청용) */
        @RequestMapping("/emailCheck")
        @ResponseBody
        public boolean emailCheck(@RequestParam("email") String email) {
            String check = MemberService.emailCheck(email); // 주의: static method 아닌 이상 this.memberService 사용해야 함
            return check != null;
        }


    // 로그인
    @PostMapping("/login")
    public String login(LoginDto loginDto,
                        HttpSession session,
                        Model model) {
        memberService.login(loginDto);
        if (loginDto != null) {
            session.setAttribute("user", loginDto);
            return "redirect:/main.jsp";
        } else {
            model.addAttribute("error", "로그인 실패");
            return "/member/login";
        }
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main.jsp";
    }

    // 회원 탈퇴
    @PostMapping("/delete")
    public String delete(@RequestParam int memberId, Model model) {
        boolean result = memberService.deleteMember(memberId);
        model.addAttribute("result", result ? "success" : "fail");
        return "redirect:/main.jsp";
    }
}
