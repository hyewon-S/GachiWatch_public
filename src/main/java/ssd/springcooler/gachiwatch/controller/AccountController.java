package ssd.springcooler.gachiwatch.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ssd.springcooler.gachiwatch.domain.Genre;
import ssd.springcooler.gachiwatch.domain.Member;
import ssd.springcooler.gachiwatch.domain.Platform;
import ssd.springcooler.gachiwatch.dto.LoginDto;
import ssd.springcooler.gachiwatch.dto.MemberRegisterDto;
import ssd.springcooler.gachiwatch.service.MemberService;

import java.util.*;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final MemberService memberService;

    @Autowired
    public AccountController(MemberService memberService) {
        this.memberService = memberService;
    }

    /** STEP1: 기본 정보 입력 화면 */
    @GetMapping("/register_email_step1")
    public String registerStep1(Model model) {
        model.addAttribute("member", new MemberRegisterDto());
        return "account/register_email_step1"; // Thymeleaf 템플릿
    }

    /** 이메일 중복 확인 API (AJAX에서 호출) */
    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> existsByEmail(@RequestParam String email) {
        System.out.println("[check-email] 요청 이메일 = " + email);
        boolean exists = memberService.existsByEmail(email);
        System.out.println("[check-email] 결과 = " + exists);
        return Map.of("exists", exists);
    }

    /** STEP1 폼 전송 → STEP2로 이동 */
    @PostMapping("/register_step2")
    public String registerStep2(@ModelAttribute("member") MemberRegisterDto dto, HttpSession session) {
        session.setAttribute("tempMember", dto); // 임시 저장
        return "account/register_step2";
    }

    /** STEP2에서 닉네임, ott, 장르 입력 받아 최종 회원가입 */
    @PostMapping("/register_result")
    public String registerFinal(@RequestParam String nickname,
                                @RequestParam(required = false) String subscribedOTTs,
                                @RequestParam(required = false) String preferredGenres,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        MemberRegisterDto dto = (MemberRegisterDto) session.getAttribute("tempMember");
        if (dto == null) {
            return "redirect:/account/register_email_step1";
        }

        // 닉네임 비었는지 체크
        if (nickname == null || nickname.trim().isEmpty()) {
            model.addAttribute("nicknameError", "닉네임은 필수입니다.");
            model.addAttribute("prevNickname", nickname);
            model.addAttribute("prevSubscribedOTTs", subscribedOTTs);
            model.addAttribute("prevPreferredGenres", preferredGenres);
            return "account/register_step2"; // 다시 폼으로
        }

        dto.setNickname(nickname);

        // OTT, Genre 문자열 → Enum 리스트 변환
        List<Platform> ottList = Optional.ofNullable(subscribedOTTs)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .map(Platform::fromDisplayName)
                        .toList())
                .orElse(Collections.emptyList());

        List<Genre> genreList = Optional.ofNullable(preferredGenres)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.stream(s.split(","))
                        .map(String::trim)
                        .map(Genre::fromDisplayName)
                        .toList())
                .orElse(Collections.emptyList());

        System.out.println("선택된 장르(raw): " + preferredGenres);

        dto.setSubscribedOtts(ottList);
        dto.setPreferredGenres(genreList);

        memberService.register(dto);
        session.removeAttribute("tempMember");

        redirectAttributes.addFlashAttribute("registerSuccess", true);
        return "redirect:/account/register_result";
    }

    /** 닉네임 중복 확인 */
    @GetMapping("/check-nickname")
    @ResponseBody
    public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
        boolean exists = memberService.isNicknameDuplicated(nickname);
        return Map.of("exists", exists);
    }

    /** 회원가입 결과 alert 페이지 */
    @GetMapping("/register_result")
    public String registerResultPage() {
        return "account/register_result";
    }

    /** 로그인 */
    @GetMapping("/login")
    public String showLoginForm() {
        return "account/login"; // 로그인 폼 보여주는 뷰 이름
    }

    /** 로그아웃 */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home";
    }

    /** 회원 탈퇴 */
    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal Member member, HttpSession session) {
        if (member != null) {
            memberService.deleteMemberByEmail(member.getEmail());
            session.invalidate(); // 세션 끊기
            SecurityContextHolder.clearContext(); // Spring Security 인증 해제
        }
        return "redirect:/home";
    }

}