package com.zhanglong.sg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhanglong.sg.service.BossService;
import com.zhanglong.sg.utils.SpringContextUtils;

@Controller
public class BossController {

	@RequestMapping(value="/boss", method = RequestMethod.GET)
	@ResponseBody
    public String list(Model model) throws Exception {

		BossService bossService = (BossService) SpringContextUtils.getBean(BossService.class);
		bossService.updateRank();

		return "ok";
	}
}
