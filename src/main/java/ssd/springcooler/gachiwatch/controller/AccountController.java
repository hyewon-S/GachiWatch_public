package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.LoginDto;
import ssd.springcooler.gachiwatch.dto.MemberRegisterDto;
import ssd.springcooler.gachiwatch.service.MemberService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final MemberService memberService;

    @Autowired
    public AccountController(MemberService memberService) {
        this.memberService = memberService;
    }

    /** ✅ STEP1: 기본 정보 입력 화면 */
    @GetMapping("/register_email_step1")
    public String registerStep1(Model model) {
        model.addAttribute("member", new MemberRegisterDto());
        return "account/register_email_step1"; // Thymeleaf 템플릿
    }

    /** ✅ STEP1 폼 전송 → STEP2로 이동 */
    @PostMapping("/register_step2")
    public String registerStep2(@ModelAttribute("member") MemberRegisterDto dto, HttpSession session) {
        session.setAttribute("tempMember", dto); // 임시 저장
        return "account/register_step2"; // step2 화면으로 이동
    }

    /** ✅ STEP2에서 닉네임, ott, 장르 입력 받아 최종 회원가입 */
    @PostMapping("/register_result")
    public String registerFinal(@RequestParam String nickname,
                                @RequestParam String subscribedOTTs,
                                @RequestParam String preferredGenres,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        MemberRegisterDto dto = (MemberRegisterDto) session.getAttribute("tempMember");
        if (dto == null) {
            return "redirect:/account/register_email_step1";
        }

        dto.setNickname(nickname);

        // OTT, Genre 문자열 → Enum 리스트 변환
        List<Platform> ottList = Arrays.stream(subscribedOTTs.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Platform::fromDisplayName)
                .toList();

        List<Genre> genreList = Arrays.stream(preferredGenres.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Genre::fromDisplayName)
                .toList();

        dto.setSubscribedOtts(ottList);
        dto.setPreferredGenres(genreList);

        memberService.register(dto);
        session.removeAttribute("tempMember");

        // ✅ 로그인 페이지로 redirect & JS alert 위한 플래그 전달
        redirectAttributes.addFlashAttribute("registerSuccess", true);
        return "redirect:/account/register_result";
    }

    @GetMapping("/register_result")
    public String registerResultPage() {
        return "account/register_result"; // ← templates/account/register_result.html
    }

//    @Value("${kakao.client_id}")
//    private String client_id;
//
//    @Value("${kakao.redirect_uri}")
//    private String redirect_uri;
//
//    @GetMapping("/register_kakao_step1")
//    public String kakaologin(Model model) {
//        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
//        model.addAttribute("location", location);
//
//        return "account/register_kakao_step1";
//    }
//
//    @GetMapping("/kakao/callback")
//    public String kakaoCallback(@RequestParam String code, HttpSession session) {
//        // code로 토큰 받고, 사용자 정보 받아서 세션에 저장
//        return "redirect:account/register_step2";
//    }



//    /** ✅ 이메일 중복 확인 (AJAX 요청용) */
//        @RequestMapping("/emailCheck")
//        @ResponseBody
//        public boolean emailCheck(@RequestParam("email") String email) {
//            String check = MemberService.emailCheck(email); // 주의: static method 아닌 이상 this.memberService 사용해야 함
//            return check != null;
//        }


    // 로그인
    @GetMapping("/login")
    public String showLoginForm() {
        return "account/login"; // 로그인 폼 보여주는 뷰 이름
    }


//    @PostMapping("/login")
//    public String login(LoginDto loginDto,
//                        HttpSession session,
//                        Model model) {
//        Member member = memberService.login(loginDto);
//
//        if (member != null) {
//            session.setAttribute("user", member);
//            return "redirect:/home"; // ← account 디렉토리 외부로 redirect //메인페이지 통합으로 인해 수정했습니다.
//        } else {
//            model.addAttribute("error", "로그인 실패");
//            return "account/login";
//        }
//    }


    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    // 회원 탈퇴
    @PostMapping("/delete")
    public String delete(@RequestParam int memberId, Model model) {
        boolean result = memberService.deleteMember(memberId);
        model.addAttribute("result", result ? "success" : "fail");
        return "redirect:/home";
    }
}