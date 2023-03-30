package com.bugjeogbugjeog.app.bugjeogbugjeog.controller;

import com.bugjeogbugjeog.app.bugjeogbugjeog.domain.vo.BusinessVO;
import com.bugjeogbugjeog.app.bugjeogbugjeog.domain.vo.MemberVO;
import com.bugjeogbugjeog.app.bugjeogbugjeog.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/*")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    //    자영업자 회원가입
    @GetMapping("join")
    public void join(Model model) {
        model.addAttribute(new MemberVO());
    }

    //    자영업자 회원가입 완료
    @PostMapping("join")
    public RedirectView join(MemberVO memberVO) {
        memberVO.setMemberPassword(new String(Base64.getEncoder().encode(memberVO.getMemberPassword().getBytes())));
        memberService.join(memberVO);
        return new RedirectView("/member/login");
    }

    //    자영업자 로그인
    @GetMapping("login")
    public void login(Model model) {
        model.addAttribute(new MemberVO());
        model.addAttribute(new BusinessVO());
    }

    //    자영업자 로그인 완료
    @PostMapping("login")
    public RedirectView login(MemberVO memberVO, HttpSession httpSession) {
        memberVO.setMemberPassword(new String(Base64.getEncoder().encode(memberVO.getMemberPassword().getBytes())));

        Long memberId = memberService.login(memberVO);

        if(memberId == null) {
            return new RedirectView("/member/login?check=false");
        } else {
            httpSession.setAttribute("memberId", memberId);
            return new RedirectView("/main/");
        }
    }

    //    유통업체 회원가입
    @GetMapping("business-join")
    public void businessJoin(Model model) {
        model.addAttribute(new BusinessVO());
    }

    //    유통업체 회원가입 완료
    @PostMapping("business-join")
    public RedirectView businessJoin(BusinessVO businessVO) {
        businessVO.setBusinessPassword(new String(Base64.getEncoder().encode(businessVO.getBusinessPassword().getBytes())));
        memberService.joinBusiness(businessVO);
        return new RedirectView("/member/login");
    }

    //    유통업체 로그인 완료
    @PostMapping("business-login")
    public RedirectView businessLogin(@RequestParam String memberEmail, @RequestParam String memberPassword, HttpSession httpSession) {
        Long businessId = memberService.businessLongin(memberEmail, new String(Base64.getEncoder().encode(memberPassword.getBytes())));

        if(businessId == null) {
            return new RedirectView("/member/login?check=false");
        } else {
            httpSession.setAttribute("businessId", businessId);
            return new RedirectView("/main/");
        }
    }

    //    계정 찾기
    @GetMapping("findAccount")
    public String findAccount() {
        return "/member/find_id";
    }

    //    계정 찾기 완료
    @PostMapping("findAccount")
    public RedirectView findAccount(@RequestParam String phoneNumber, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("phoneNumber", phoneNumber);
        return new RedirectView("/member/findResultAccount");
    }

    //    계정 찾기 결과 페이지
    @GetMapping("findResultAccount")
    public String findResultAccount(@RequestParam String phoneNumber, Model model) {
        model.addAttribute("findEmailDTO", memberService.findAccount(phoneNumber));
        return "/member/find_id_end";
    }

    //    자영업자 비밀번호 변경
    @GetMapping("memberPasswordChange")
    public String changePassword(@RequestParam String memberEmail, Model model) {
        model.addAttribute("memberEmail", memberEmail);
        return "/member/new_password";
    }

//        자영업자 비밀번호 변경 완료
    @PostMapping("memberPasswordChange")
    public RedirectView changePassword(@RequestParam String memberEmail, @RequestParam String memberPassword) {
        memberService.changePassword(memberEmail, new String(Base64.getEncoder().encode(memberPassword.split(",")[1].getBytes())));
        return new RedirectView("/member/completeChangePassword");
    }

//        사업자 비밀번호 변경
    @GetMapping("businessPasswordChange")
    public String businessChangePassword(String businessEmail, Model model) {
        model.addAttribute("businessEmail", businessEmail);
        return "/member/business_new_password";
    }

//        사업자 비밀번호 변경 완료
    @PostMapping("businessPasswordChange")
    public RedirectView businessChangePassword(@RequestParam String businessEmail, @RequestParam String businessPassword) {
        memberService.businessChangePassword(businessEmail, new String(Base64.getEncoder().encode(businessPassword.split(",")[1].getBytes())));
        return new RedirectView("/member/completeChangePassword");
    }

//        비밀번호 변경 완료 페이지
    @GetMapping("completeChangePassword")
    public String completeChangePassword() {
        return "/member/newPW_complete";
    }

//    로그아웃
    @GetMapping("logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "/main/";
    }
}