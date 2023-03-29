package com.bugjeogbugjeog.app.bugjeogbugjeog.controller;


import com.bugjeogbugjeog.app.bugjeogbugjeog.domain.dto.InterestingCompanyDTO;
import com.bugjeogbugjeog.app.bugjeogbugjeog.domain.dto.PageDTO;
import com.bugjeogbugjeog.app.bugjeogbugjeog.domain.vo.Criteria;
import com.bugjeogbugjeog.app.bugjeogbugjeog.service.InterestingCompanyService;
import com.bugjeogbugjeog.app.bugjeogbugjeog.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/interesing/*")
@RequiredArgsConstructor
@Slf4j
public class InterestingCompanyController {

    private final InterestingCompanyService interestingCompanyService;
    private final MyPageService myPageService;

    @GetMapping("company")
    public String company(Model model, Criteria criteria){
        log.info(interestingCompanyService.findAllToInterestingCompany(criteria).toString());
        model.addAttribute("memberVO",myPageService.memberInfo(1L));
        model.addAttribute("interestingCompanyDTOs", interestingCompanyService.findAllToInterestingCompany(criteria));
        model.addAttribute("pageDTO", new PageDTO(criteria, interestingCompanyService.count()));

        return "mypage/specific/personalFavoriteList";
    }

}
