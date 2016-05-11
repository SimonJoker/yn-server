package com.yunnong.controller;

import com.yunnong.logic.FileLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * Created by joker on 2016/4/11.
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @RequestMapping(value = "/con_img", method = RequestMethod.POST)
    public @ResponseBody
    String consultantHead(@RequestParam String pid, @RequestParam String token,
                                  @RequestParam MultipartFile file,  HttpSession session) {
        FileLogic fileLogic = new FileLogic();
        String callback = fileLogic.uploadConsultImage(pid, file);
        fileLogic.destroyResource();
        return callback;
    }
}
