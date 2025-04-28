@Controller
@RequestMapping("/member")
public class AccountController {

    private final MemberService memberService;

    @Autowired
    public AccountController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    @PostMapping("/register")
    public String register(MemberRegisterDto regiDto, Model model) {
        memberService.register(regiDto);
        model.addAttribute("result", "success");
        return "/member/registerResult";
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        LoginDto loginDto = memberService.login(email, password);
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
