//package ssd.springcooler.gachiwatch.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ssd.springcooler.gachiwatch.dto.EmailNotiRequestDto;
//import ssd.springcooler.gachiwatch.service.EmailNotiService;
//
//@RestController
//@RequestMapping("/api/v1/noti")
//public class EmailNotiController {
//    @Autowired
//    private EmailNotiService emailNotiService;
//
////    @PostMapping("/api/v1/noti")
////    public String sendQna(@RequestBody EmailNotiDto emailNotiDto) {
////        emailNotiService.sendMessage(emailNotiDto.getEmail(), emailNotiDto.getContent());
////        return "이메일이 성공적으로 전송되었습니다.";
////    }
//
//    @PostMapping
//    public String sendEmailRecommendation(@RequestBody EmailNotiRequestDto dto) {
//        try {
//            emailNotiService.sendRecommendationEmail(dto);
//            return "추천 콘텐츠가 이메일로 전송되었습니다.";
//        } catch (Exception e) {
//            return "이메일 전송 실패: " + e.getMessage();
//        }
//    }
//
//}
