package com.pyt.util;

/**
 * Created by PC on 2020/7/3.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPUtil {
    private String host;//sftp服务器ip
    private String username;//用户名
    private String password;//密码
    private String privateKey;//密钥文件路径
    private String passphrase;//密钥口令
    private static int port = 22;//默认的sftp端口号22

    private  SFTPUtil(String host,String username,String password,String privateKey, String passphrase, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
        this.port = port;
    }

    private static SFTPUtil sftpUtil = new SFTPUtil("39.106.29.128","sftpuser","number92013",null,null,port);
    private static ChannelSftp channelsftp =  sftpUtil.connectSFTP();

    public static SFTPUtil  getSFTPUtil() {
        if(null == sftpUtil){
            sftpUtil = new SFTPUtil("39.106.29.128","sftpuser","number92013",null,null,port);
        }
        return sftpUtil;
    }
    public static ChannelSftp getChannelSftp(){
        if(null == channelsftp){
            channelsftp =  getSFTPUtil().connectSFTP();
        }
       return channelsftp;
    }
    /**
     * 获取连接
     * @return channel
     */
    public ChannelSftp connectSFTP() {
        JSch jsch = new JSch();
        Channel channel = null;
        System.out.println("--------privateKey:"+privateKey);
        try {
            if (privateKey != null && !"".equals(privateKey)) {
                //使用密钥验证方式，密钥可以使有口令的密钥，也可以是没有口令的密钥
                if (passphrase != null && "".equals(passphrase)) {
                    jsch.addIdentity(privateKey, passphrase);
                } else {
                    jsch.addIdentity(privateKey);
                }
            }
            Session session = jsch.getSession(username, host, port);
            if (password != null && !"".equals(password)) {
                session.setPassword(password);
            }

            //Console 里面打印连接的更多相关信息
            com.jcraft.jsch.Logger logger = new SettleLogger();
            JSch.setLogger(logger);

            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");// do not verify host key
            session.setConfig(sshConfig);
            // session.setTimeout(timeout);
            session.setServerAliveInterval(92000);
            session.connect();
            //参数sftp指明要打开的连接是sftp连接
            channel = session.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            System.out.println("connectSFTP:"+e);
        }
        return (ChannelSftp) channel;
    }

    /**
     * 上传文件
     *
     * @param directory
     *            上传的目录
     * @param uploadFile
     *            要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            try{
                sftp.cd(directory);
            }catch(Exception e){
                sftp.mkdir(directory);
                sftp.cd(directory);
            }
            System.out.println(uploadFile);
            File file = new File(uploadFile);
            System.out.println(directory+","+uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            System.out.println("upload:"+e);
        }
    }

    /**
     * 上传文件
     *
     * @param directory
     *            上传的目录
     *            要上传的文件
     * @param sftp
     */
    public void upload(String directory, String fileName, InputStream inputStream, ChannelSftp sftp) {
        try {
            try{
                sftp.cd(directory);
            }catch(Exception e){
                sftp.mkdir(directory);
                sftp.cd(directory);
            }
            sftp.put(inputStream, fileName);
        } catch (Exception e) {
            System.out.println("upload:"+e);
        }
    }

    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     * @param sftp
     */
    public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.get(downloadFile,saveFile);
        } catch (Exception e) {
            System.out.println("download:"+e);
        }
    }

    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            System.out.println("delete:"+e);
        }
    }

    public void disconnected(ChannelSftp sftp){
        if (sftp != null) {
            try {
                sftp.getSession().disconnect();
            } catch (JSchException e) {
                System.out.println("disconnected:"+e);
            }
            sftp.disconnect();
        }
    }

    public static  void  main(String []arg){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String directory = "/opt/tomcat-7/webapps/ROOT/attachment/"+sdf.format(new Date())+"/";//默认地址
        String uploadfilepath = "C:\\迅雷下载\\low(4).mp4";
        SFTPUtil sftpUtil = SFTPUtil.getSFTPUtil();
        ChannelSftp channelsftp =  sftpUtil.connectSFTP();
        if(channelsftp!=null) {
            sftpUtil.upload(directory,uploadfilepath,channelsftp);
            //下载和删除就不写了 反正都是写一下服务器的文件路径 需要操作的文件 最后再写个channelsftp就好了
            sftpUtil.disconnected(channelsftp);
        }
    }


}

//Console 里面打印连接的更多相关信息
class SettleLogger implements com.jcraft.jsch.Logger {
    public boolean isEnabled(int level) {
        return true;
    }

    public void log(int level, String msg) {
        System.out.println(msg);
    }
}
