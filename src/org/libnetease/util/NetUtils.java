package org.libnetease.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 网络相关操作
 * @author lance
 *
 */
public class NetUtils {
	private final static String TAG="NetUtils";

	public static final int NET_CONNECTED = 1;// 有网络
	public static final int NET_NO_CONNECT = 0;

	/** 创建一个线程池 */
	public static ThreadPoolExecutor createThreadPoolExecutor(int coreSize,int maxSize,long activeTime){
		return new ThreadPoolExecutor(coreSize, maxSize, activeTime, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}
	/**
	 * 检查网络连接状态 return 只判断是否连接
	 */
	public static int checkNetwork(Context context) {
		ConnectivityManager netManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = netManager.getActiveNetworkInfo();
		if (net != null && net.isConnected()) {
			return NET_CONNECTED;
		}
		return NET_NO_CONNECT;
	}

	/**
	 * 下载指定文件名的图片并保存到sd卡的指定文件夹 该方法执行了图片检测---速度稍慢
	 * 
	 * @param fileName
	 * @param 保存图片的文件夹
	 * @return 是否下载并保存成功
	 */
	public static boolean downloadImage(String fileName, String saveDir,
			String remoteUrl) {
		RandomAccessFile raf = null;
		try {
			File file = null;
			File saveDirPath = new File(saveDir);
			if (!saveDirPath.exists()) {
				saveDirPath.mkdirs();// 创建多级目录
			}
			file = new File(saveDir + fileName);
			long netFileLength = getNetFileLength(remoteUrl);// 没有网络的时候返回-1
			if(netFileLength==-1||netFileLength==-2){
				return false;
			}
			if (!file.exists()) {// 如果不存在创建新文件
				file.createNewFile();
				raf = new RandomAccessFile(file, "rw");
			} else {
				if (file.length() == netFileLength) {// 如果文件大小相同那么不需要下载
					return true;
				} else {
					file.delete();// 先删除原图片
					file.createNewFile();// 然后创建新文件写入图片数据
					raf = new RandomAccessFile(file, "rw");
				}
			}
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(remoteUrl);
			// 不添加头也能实现断点下载!
			Header header = new BasicHeader("Range", "bytes=" + 0 + "-"
					+ netFileLength);
			request.addHeader(header);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			raf.seek(0);
			InputStream is = entity.getContent();
			if (is != null) {
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					raf.write(buf, 0, ch);
				}
			}
			is.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 下载图片结果
	 * @param fileName
	 * @param saveDir
	 * @param remoteDir
	 * @return 1下载成功 -1网络问题; 0 网络资源不存在 2保存目录没有权限 3下载失败
	 */
	public static int downloadImage2(String fileName, String saveDir,
			String remoteDir) {
		int result=1;
		RandomAccessFile raf = null;
		try {
			String remoteUrl = remoteDir + fileName;
			File file = null;
			File saveDirPath = new File(saveDir);
			if (!saveDirPath.exists()) {
				saveDirPath.mkdirs();
			}
			file = new File(saveDir + fileName);
			long netFileLength = getNetFileLength(remoteDir + fileName);// 没有网络的时候返回-1
			if (!file.exists()) {// 如果不存在创建新文件
				file.createNewFile();
				raf = new RandomAccessFile(file, "rw");
			} else {
				// 获取本地文件长度
				if (netFileLength == -1) {// 此时代表没有网络
					result= -1;
				} else if (file.length() == netFileLength) {// 如果文件大小相同那么不需要下载
					result= 1;
				} else {
					file.delete();// 先删除原图片
					file.createNewFile();// 然后创建新文件写入图片数据
					raf = new RandomAccessFile(file, "rw");
				}
			}
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(remoteUrl);
			// 不添加头也能实现断点下载!
			Header header = new BasicHeader("Range", "bytes=" + 0 + "-"
					+ netFileLength);
			request.addHeader(header);
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			raf.seek(0);
			InputStream is = entity.getContent();
			if (is != null) {
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					raf.write(buf, 0, ch);
				}
			}
			is.close();
			return 1;
		} catch (FileNotFoundException e) {
			result=0;
			Log.w(TAG, e.getMessage());
		}catch (IOException e) {
			result=3;
			Log.w(TAG, e.getMessage());
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 下载图片并缓存到sd卡
	 * @param urlDir
	 * @param cacheDir
	 * @param fileName
	 * @return
	 */
	public static Drawable loadImageFromUrl(String urlDir, String cacheDir,
			String fileName) {
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				if (!cacheDir.endsWith("/")) {
					cacheDir += "/";
				}
				File dirFile = new File(cacheDir);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				File file = new File(cacheDir + fileName);// 需要缓存的文件
				if (file.exists()) {
					FileInputStream fis = new FileInputStream(file);
					// 第二个参数没有意义;用来调试时使用的
					Drawable d = Drawable.createFromStream(fis, "src");
					fis.close();
					if(d==null){
						file.delete();//这是图片文件未下载完的情况---删除文件重新下载
						return loadImageFromUrl(urlDir+fileName, cacheDir, fileName);
					}else{
						return d;
					}
				}
				URL u = new URL(urlDir+fileName);
				InputStream in = u.openStream();
				DataInputStream dis = new DataInputStream(in);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buff = new byte[1024];
				int count = 0;
				while ((count = dis.read(buff)) != -1) {
					fos.write(buff, 0, count);
				}
				fos.flush();
				dis.close();
				fos.close();
				in.close();
				// 此处已经写入了文件---重新调用该方法加载图片
				return loadImageFromUrl(urlDir+fileName, cacheDir, fileName);
			} else {
				URL u = new URL(urlDir+fileName);
				InputStream in = u.openStream();
				Drawable d = Drawable.createFromStream(in, "src");
				in.close();
				return d;
			}
		}catch(Exception e){
			e.printStackTrace();
		}catch (java.lang.OutOfMemoryError e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			System.exit(0);//这里清理一下内存
		}
		return null;
	}

	// 获取远程文件的总大小
	private static long getNetFileLength(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			StatusLine status=response.getStatusLine();
			if(status.getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				// 获取远程文件的大小
				return entity.getContentLength();
			}else if(status.getStatusCode()==HttpStatus.SC_NOT_FOUND){
				return -2;//没有该文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;// 如果没有网络返回-1
	}
	
	/** 文件下载 保存路径 文件名 */
	public static boolean downFile(String url, String saveDir, String fileName) {
		File dir = new File(saveDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(saveDir+"/"+fileName);
		if(file.exists()){
			long len=getNetFileLength(url);
			if(file.length()==len){
				return true;
			}else{
				file.delete();
			}
		}
		InputStream input = null;
		OutputStream out = null;
		try {
			file.createNewFile();
			HttpGet get = new HttpGet(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);
			input = response.getEntity().getContent();
			out = new FileOutputStream(file);
			byte[] brr = new byte[1024];
			int i = 0;
			while ((i = input.read(brr)) != -1) {
				System.out.println("偏移量--->" + i);
				out.write(brr, 0, i);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			try {
				if(input!=null){
					input.close();
				}
				if(out!=null){
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static InputStream getInputStream(String url) {
		InputStream input = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			input = response.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return input;
	}
	/**
	 * 获取电话呼叫状态
	 * 
	 * @param context
	 * @return CALL_STATE_IDLE空闲 CALL_STATE_RINGING响铃 CALL_STATE_OFFHOOK摘机
	 */
	public static int getCallState(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getCallState();
	}

	/**
	 * 获取电话方位
	 * 
	 * @param context
	 * @return
	 */
	public static CellLocation getCellLocation(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getCellLocation();
	}

	/**
	 * 获取设备序列号
	 * 
	 * @param context
	 * @return GSM手机的IMEI CDMA手机的MEID 如果返回null表示设备Id无效
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getDeviceId();
	}

	/**
	 * 获取设备的软件版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceSoftwareVersion(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getDeviceSoftwareVersion();
	}

	/**
	 * 获取手机号
	 * 
	 * @param context
	 * @return GSM手机的MSISDN 如果没有手机号返回null
	 */
	public static String getLine1Number(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getLine1Number();
	}

	/**
	 * 获取附近的电话信息
	 * 
	 * @param context
	 * @return 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
	 */
	public static List<NeighboringCellInfo> getNeighboringCellInfo(
			Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNeighboringCellInfo();
	}

	/**
	 * 获取ISO标准的国家 码;即国际长途区号 仅在搜索到网络后有效 CDMA网络情况未知
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetwordCountryIso(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkCountryIso();
	}

	/**
	 * MCC+MNC(mobile country code + mobile network code)
	 * 
	 * @param context
	 * @return 注意：仅当用户已在网络注册时有效。 在CDMA网络中结果也许不可靠。
	 */
	public static String getNetworkOperator(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkCountryIso();
	}

	/**
	 * 按照字母次序的current registered operator(当前已注册的用户)的名字
	 * 
	 * @param context
	 * @return 注意：仅当用户已在网络注册时有效。 在CDMA网络中结果也许不可靠。
	 */
	public static String getNetworkOperatorName(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkOperatorName();
	}

	/**
	 * 获取当前使用的网络类型
	 * 
	 * @param context
	 * @return NETWORK_TYPE_UNKNOWN 网络类型未知 0 NETWORK_TYPE_GPRS GPRS网络 1
	 *         NETWORK_TYPE_EDGE EDGE网络 2 NETWORK_TYPE_UMTS UMTS网络 3
	 *         NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4 NETWORK_TYPE_EVDO_0
	 *         EVDO网络, revision 0. 5 NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6
	 *         NETWORK_TYPE_1xRTT 1xRTT网络 7 NETWORK_TYPE_HSDPA HSDPA网络 8
	 *         NETWORK_TYPE_HSUPA HSUPA网络 9 NETWORK_TYPE_HSPA HSPA网络 10
	 */
	public static int getNetwordType(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkType();
	}

	/**
	 * 获取手机网络的信号类型
	 * 
	 * @param context
	 * @return PHONE_TYPE_NONE 无信号 PHONE_TYPE_GSM GSM信号 PHONE_TYPE_CDMA CDMA信号
	 */
	public static int getPhoneType(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getPhoneType();
	}

	/**
	 * 获取ISO国家码，相当于提供SIM卡的国家码。
	 * 
	 * @param context
	 * @return
	 */
	public static String getSimCountryIso(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSimCountryIso();
	}

	/**
	 * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字. SIM卡的状态必须是
	 * SIM_STATE_READY(使用getSimState()判断).
	 * 
	 * @param context
	 * @return
	 */
	public static String getSimOperator(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSimOperator();
	}

	/**
	 * 获取网络运营商名字
	 * 
	 * @param context
	 * @return 注意SIM卡的状态必须是SIM_STATE_READY
	 */
	public static String getSimOperatorName(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSimOperatorName();
	}

	/**
	 * SIM卡的序列号： 需要权限：READ_PHONE_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static String getSimSerialNumber(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSimOperatorName();
	}

	/**
	 * 获取SIM卡的状态 SIM_STATE_UNKNOWN 未知状态 0 SIM_STATE_ABSENT 没插卡 1
	 * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2 SIM_STATE_PUK_REQUIRED
	 * 锁定状态，需要用户的PUK码解锁 3 SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
	 * SIM_STATE_READY 就绪状态 5
	 * 
	 * @param context
	 * @return
	 */
	public static int getSimState(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSimState();
	}

	/**
	 * 唯一的用户ID： 例如：IMSI(国际移动用户识别码) for a GSM phone. 需要权限：READ_PHONE_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static String getSubscriberId(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSubscriberId();
	}

	/**
	 * 取得和语音邮件相关的标签，即为识别符 需要权限：READ_PHONE_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static String getVoiceMailAlohaTag(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getVoiceMailAlphaTag();
	}

	/**
	 * 获取语音邮件号码： 需要权限：READ_PHONE_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static String getVoiceMailNumber(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getVoiceMailNumber();
	}

	/**
	 * ICC卡是否存在
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasIccCard(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.hasIccCard();
	}

	/**
	 * 是否漫游: (在GSM用途下)
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetwordRoaming(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.isNetworkRoaming();
	}
}
