package com.zhanglong.sg.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhanglong.sg.dao.WhiteListDao;
import com.zhanglong.sg.dao.VersionDao;
import com.zhanglong.sg.entity.Version;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;

@Controller
public class VController {

	@Resource
	private WhiteListDao imeiDao;

	@Resource
	private VersionDao versionDao;

	@RequestMapping(value="/v", method = RequestMethod.GET)
	@ResponseBody
    public String list(@RequestParam(value = "code" , required = false) int code, @RequestParam(value = "imei" , required = false) String imei, @RequestParam(value = "channel" , required = false) int channel, Model model) throws Exception {

		boolean find = this.imeiDao.find(imei);

		List<Version> list = this.versionDao.findAll(code);

		Version apk = null;
		List<Version> update_list = new ArrayList<Version>();
		for (Version versionTable : list) {

			if (versionTable.getChannel() != channel) {
				continue;
			}
			if (versionTable.getVersionEnable() != Version.ENABLE && !find) {
				continue;
			}

			if (versionTable.getVersionType() == Version.TYPE_APK) {
				apk = versionTable;
			} else if (versionTable.getVersionType() == Version.TYPE_ZIP) {
				update_list.add(versionTable);
			}
		}

		Result result = new Result();

		if (apk != null) {
			result.setValue("apk", apk);
		} else {
			result.setValue("update_list", update_list);
		}

		return Response.marshalSuccess(0, result.toMap());
	}
}
