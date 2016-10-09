package com.yuqincar.install;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.yuqincar.utils.DateUtils;

public class Test {

	public static void main(String[] args) {
		System.out.println(DateUtils.getMinDate(DateUtils.getYMD("2016-09-20")).getTime());
		System.out.println(DateUtils.getMaxDate(DateUtils.getYMD("2016-10-04")).getTime());
	}
	
	private static void testURLEncode(){
		String url="http://localhost:8080/Web/orderapp/OrderApp_searchCustomerOrganization.action?keyword=";
		try{
			url=url+URLEncoder.encode("大学", "utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(url);
	}
	
	private static void dealFuniuPic(){
		String sourceFolder="C:\\Users\\chenhengxin\\Desktop\\2016.5.24-2016.5.31";
		String destinationFolder="E:\\科研工作\\伏牛溪项目\\pic\\";
		
		File file=new File(sourceFolder);
		dealFuniuPic(file,destinationFolder);
	}
	
	private static void dealFuniuPic(File file,String destinationFolder){
		if(file.isDirectory()){
			File fileNames[]=file.listFiles();
			for(File f:fileNames){
				if(f.isDirectory())
					dealFuniuPic(f,destinationFolder);
				else{
					Date date=new Date(f.lastModified());
					if(date.getHours()<9 || date.getHours()>18)
						continue;
					File destFile=new File(destinationFolder+String.valueOf(f.lastModified())+".jpg");
					try{
						FileUtils.copyFile(f, destFile);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static void baiduPush() throws PushClientException, PushServerException {
		/*1. 创建PushKeyPair
         *用于app的合法身份认证
         *apikey和secretKey可在应用详情中获取
         */
		String apiKey="DNqF5kiGbVaXBCQvK7kk6miU";
		String secretKey="yzP3nylp4xoflQTm2I1AVxBymalKE5sz";
		PushKeyPair pair=new PushKeyPair(apiKey, secretKey);
		
		// 2. 创建BaiduPushClient，访问SDK接口
		BaiduPushClient pushClient=new BaiduPushClient(pair,BaiduPushConstants.CHANNEL_REST_URL);
		
		// 3. 注册YunLogHandler，获取本次请求的交互信息
        pushClient.setChannelLogHandler (new YunLogHandler () {
            public void onHandle (YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        
        try {
        	//message为推送的具体内容，具体格式参见http://push.baidu.com/doc/restapi/msg_struct
        	JSONObject notification = new JSONObject();
    		notification.put("title", "渝勤汽车");
    		notification.put("description","定位到等待服务的某个订单");
/*    		notification.put("notification_builder_id", 0);
    		notification.put("notification_basic_style", 4);
    		notification.put("open_type", 1);
    		notification.put("url", "http://push.baidu.com");*/
    		JSONObject customContent = new JSONObject();
    		customContent.put("orderStatus", "WAIT");  //订单状态
    		customContent.put("id", 24); //订单id
    		notification.put("custom_content", customContent);
			
			// 4. 设置请求参数，创建请求实例
			PushMsgToSingleDeviceRequest request=new PushMsgToSingleDeviceRequest().
					addChannelId("4526180907281016587"). //设备token
					addDeviceType(3).  //3 is for android
					addMessageType(1). //1标示推送为通知
					addMessage(notification.toString());
			
			// 5. 执行Http请求
			PushMsgToSingleDeviceResponse response=pushClient.pushMsgToSingleDevice(request);
			
			// 6. Http请求返回值解析
			System.out.println("msgId:"+response.getMsgId()+",sendTime:"+response.getSendTime());
			
		} catch (PushClientException e) {
            //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
            //'true' 表示抛出, 'false' 表示捕获。
            if (BaiduPushConstants.ERROROPTTYPE) { 
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMsg: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
        
	}
}
