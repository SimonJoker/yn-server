package com.yunnong.logic;

import com.yunnong.config.FilePathConf;
import com.yunnong.dao.OperDao;
import com.yunnong.utils.CharUtil;
import com.yunnong.utils.LogUtils;
import com.yunnong.utils.UidUtils;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by joker on 2016/4/11.
 */
public class FileLogic {
    private final static Logger logger = LoggerFactory.getLogger(FileLogic.class);

    /**
     * 支持的图片格式
     */
   private final static String[] IMAGE_TYPE = new String[]{"png", "jpg"};

    FilePathConf filePathConf = new FilePathConf();
    private OperDao operDao;

    public FileLogic(){
        ClassPathXmlApplicationContext ctxFile =
                new ClassPathXmlApplicationContext("META-INF/image-path-conf.xml");
        filePathConf = ctxFile.getBean("filePathConf", FilePathConf.class);
        operDao = new OperDao();
    }

    /**
     * @param pid
     * @return 数据库中是否已存在该用户
     */
    public boolean isConsultExist(String pid){
        String sql = "SELECT COUNT(pid) FROM sys_consultant WHERE pid=" + pid;
        ResultSet rs = operDao.findExcuteSql(sql);
        try {
            if (rs.getInt("COUNT(pid)") > 0){
                return true;
            }
        } catch (SQLException e) {
            LogUtils.LogError(logger, "count consult " + pid + "failed", e);
        }
        return false;
    }


    /**
     * 判断上传的文件是否符合指定文件类型
     * @param type
     * @return
     */
    public boolean isRightType(String type){
        boolean result = false;
        for (String ty : IMAGE_TYPE){
            if (ty.equals(type)){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 判断”文件“是否存在
     * @param dir
     * @return
     */
    public boolean isFileExist(String dir){
        File file = new File(dir);
        if (file.exists() && !file.isDirectory()){
            return true;
        }
        return false;
    }

    /**
     * 删除已经存在的文件
     * @param dir
     */
    public void deletFile(String dir){
        File file = new File(dir);
        if (file.exists() && !file.isDirectory()){
            file.delete();
        }
    }


    /**
     * @param pid pid 为空则创建用户,pid 不为空则认为是修改用户头像
     * @param file 上传的用户头像
     * @return
     */
    public String uploadConsultImage(String pid, MultipartFile file){
        JSONObject callback = new JSONObject();
        callback.put("api", "con_img");
        callback.put("result", 602);
        if (!file.isEmpty()){
            String ty = CharUtil.cut3String(file.getOriginalFilename(), "\\.", 2);
            if (isRightType(ty)){
                boolean flag = false;
                if (pid == null || "".equals(pid)){
                    pid = UidUtils.chPIdNext() + ""; //pid 为空 认为时创建新用户，而不是更改用户头像
                    flag = true;
                }
                if ( flag || isConsultExist(pid)){ //上传照片时用户还没有创建则创建 不检查数据库
                    String img_parent = filePathConf.getConsultantImagePath();
                    String imgName = pid +"\\." + ty;
                    String con_img_path= img_parent +"/" + imgName;
                    if (isFileExist(con_img_path)){
                        deletFile(con_img_path); //如果上传的文件已经存在则 删除旧文件 保存新的文件
                    }
                    try {
                        //将文件写入文件系统
                        FileUtils.copyInputStreamToFile(file.getInputStream()
                                , new File(img_parent, imgName));
                        //返回用户头像url
                        callback.put("photo", filePathConf.getConsultantImageUrl() +"/" +imgName );
                        callback.put("pid", pid);
                        callback.replace("result", 200);
                    } catch (IOException e) {
                        callback.replace("result", 603); //保存头像失败
                        LogUtils.LogError(logger, "save file " + file.getOriginalFilename() + "failed", e);
                    }
                }else {
                    callback.replace("result", 605);} //需要修改的 pid  不存在
            }else {callback.replace("result", 604);}  //上传的文件类型不正确
        }
        return callback.toString();
    }

    public void destroyResource(){
        operDao.destroyPool();
    }
}
