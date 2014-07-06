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
 * ������ز���
 * @author lance
 *
 */
public class NetUtils {
	private final static String TAG="NetUtils";

	public static final int NET_CONNECTED = 1;// ������
	public static final int NET_NO_CONNECT = 0;

	/** ����һ���̳߳� */
	public static ThreadPoolExecutor createThreadPoolExecutor(int coreSize,int maxSize,long activeTime){
		return new ThreadPoolExecutor(coreSize, maxSize, activeTime, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}
	/**
	 * �����������״̬ return ֻ�ж��Ƿ�����
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
	 * ����ָ���ļ�����ͼƬ�����浽sd����ָ���ļ��� �÷���ִ����ͼƬ���---�ٶ�����
	 * 
	 * @param fileName
	 * @param ����ͼƬ���ļ���
	 * @return �Ƿ����ز�����ɹ�
	 */
	public static boolean downloadImage(String fileName, String saveDir,
			String remoteUrl) {
		RandomAccessFile raf = null;
		try {
			File file = null;
			File saveDirPath = new File(saveDir);
			if (!saveDirPath.exists()) {
				saveDirPath.mkdirs();// �����༶Ŀ¼
			}
			file = new File(saveDir + fileName);
			long netFileLength = getNetFileLength(remoteUrl);// û�������ʱ�򷵻�-1
			if(netFileLength==-1||netFileLength==-2){
				return false;
			}
			if (!file.exists()) {// ��������ڴ������ļ�
				file.createNewFile();
				raf = new RandomAccessFile(file, "rw");
			} else {
				if (file.length() == netFileLength) {// ����ļ���С��ͬ��ô����Ҫ����
					return true;
				} else {
					file.delete();// ��ɾ��ԭͼƬ
					file.createNewFile();// Ȼ�󴴽����ļ�д��ͼƬ����
					raf = new RandomAccessFile(file, "rw");
				}
			}
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(remoteUrl);
			// �����ͷҲ��ʵ�ֶϵ�����!
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
	 * ����ͼƬ���
	 * @param fileName
	 * @param saveDir
	 * @param remoteDir
	 * @return 1���سɹ� -1��������; 0 ������Դ������ 2����Ŀ¼û��Ȩ�� 3����ʧ��
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
			long netFileLength = getNetFileLength(remoteDir + fileName);// û�������ʱ�򷵻�-1
			if (!file.exists()) {// ��������ڴ������ļ�
				file.createNewFile();
				raf = new RandomAccessFile(file, "rw");
			} else {
				// ��ȡ�����ļ�����
				if (netFileLength == -1) {// ��ʱ����û������
					result= -1;
				} else if (file.length() == netFileLength) {// ����ļ���С��ͬ��ô����Ҫ����
					result= 1;
				} else {
					file.delete();// ��ɾ��ԭͼƬ
					file.createNewFile();// Ȼ�󴴽����ļ�д��ͼƬ����
					raf = new RandomAccessFile(file, "rw");
				}
			}
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(remoteUrl);
			// �����ͷҲ��ʵ�ֶϵ�����!
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
	 * ����ͼƬ�����浽sd��
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
				File file = new File(cacheDir + fileName);// ��Ҫ������ļ�
				if (file.exists()) {
					FileInputStream fis = new FileInputStream(file);
					// �ڶ�������û������;��������ʱʹ�õ�
					Drawable d = Drawable.createFromStream(fis, "src");
					fis.close();
					if(d==null){
						file.delete();//����ͼƬ�ļ�δ����������---ɾ���ļ���������
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
				// �˴��Ѿ�д�����ļ�---���µ��ø÷�������ͼƬ
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
			System.exit(0);//��������һ���ڴ�
		}
		return null;
	}

	// ��ȡԶ���ļ����ܴ�С
	private static long getNetFileLength(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			StatusLine status=response.getStatusLine();
			if(status.getStatusCode()==HttpStatus.SC_OK){
				HttpEntity entity = response.getEntity();
				// ��ȡԶ���ļ��Ĵ�С
				return entity.getContentLength();
			}else if(status.getStatusCode()==HttpStatus.SC_NOT_FOUND){
				return -2;//û�и��ļ�
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;// ���û�����緵��-1
	}
	
	/** �ļ����� ����·�� �ļ��� */
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
				System.out.println("ƫ����--->" + i);
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
	 * ��ȡ�绰����״̬
	 * 
	 * @param context
	 * @return CALL_STATE_IDLE���� CALL_STATE_RINGING���� CALL_STATE_OFFHOOKժ��
	 */
	public static int getCallState(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getCallState();
	}

	/**
	 * ��ȡ�绰��λ
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
	 * ��ȡ�豸���к�
	 * 
	 * @param context
	 * @return GSM�ֻ���IMEI CDMA�ֻ���MEID �������null��ʾ�豸Id��Ч
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getDeviceId();
	}

	/**
	 * ��ȡ�豸������汾��
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
	 * ��ȡ�ֻ���
	 * 
	 * @param context
	 * @return GSM�ֻ���MSISDN ���û���ֻ��ŷ���null
	 */
	public static String getLine1Number(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getLine1Number();
	}

	/**
	 * ��ȡ�����ĵ绰��Ϣ
	 * 
	 * @param context
	 * @return ��ҪȨ�ޣ�android.Manifest.permission#ACCESS_COARSE_UPDATES
	 */
	public static List<NeighboringCellInfo> getNeighboringCellInfo(
			Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNeighboringCellInfo();
	}

	/**
	 * ��ȡISO��׼�Ĺ��� ��;�����ʳ�;���� �����������������Ч CDMA�������δ֪
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
	 * @return ע�⣺�����û���������ע��ʱ��Ч�� ��CDMA�����н��Ҳ���ɿ���
	 */
	public static String getNetworkOperator(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkCountryIso();
	}

	/**
	 * ������ĸ�����current registered operator(��ǰ��ע����û�)������
	 * 
	 * @param context
	 * @return ע�⣺�����û���������ע��ʱ��Ч�� ��CDMA�����н��Ҳ���ɿ���
	 */
	public static String getNetworkOperatorName(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkOperatorName();
	}

	/**
	 * ��ȡ��ǰʹ�õ���������
	 * 
	 * @param context
	 * @return NETWORK_TYPE_UNKNOWN ��������δ֪ 0 NETWORK_TYPE_GPRS GPRS���� 1
	 *         NETWORK_TYPE_EDGE EDGE���� 2 NETWORK_TYPE_UMTS UMTS���� 3
	 *         NETWORK_TYPE_CDMA CDMA����,IS95A �� IS95B. 4 NETWORK_TYPE_EVDO_0
	 *         EVDO����, revision 0. 5 NETWORK_TYPE_EVDO_A EVDO����, revision A. 6
	 *         NETWORK_TYPE_1xRTT 1xRTT���� 7 NETWORK_TYPE_HSDPA HSDPA���� 8
	 *         NETWORK_TYPE_HSUPA HSUPA���� 9 NETWORK_TYPE_HSPA HSPA���� 10
	 */
	public static int getNetwordType(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getNetworkType();
	}

	/**
	 * ��ȡ�ֻ�������ź�����
	 * 
	 * @param context
	 * @return PHONE_TYPE_NONE ���ź� PHONE_TYPE_GSM GSM�ź� PHONE_TYPE_CDMA CDMA�ź�
	 */
	public static int getPhoneType(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getPhoneType();
	}

	/**
	 * ��ȡISO�����룬�൱���ṩSIM���Ĺ����롣
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
	 * ��ȡSIM���ṩ���ƶ���������ƶ�������.5��6λ��ʮ��������. SIM����״̬������
	 * SIM_STATE_READY(ʹ��getSimState()�ж�).
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
	 * ��ȡ������Ӫ������
	 * 
	 * @param context
	 * @return ע��SIM����״̬������SIM_STATE_READY
	 */
	public static String getSimOperatorName(Context context) {
		TelephonyManager teleManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return teleManager.getSimOperatorName();
	}

	/**
	 * SIM�������кţ� ��ҪȨ�ޣ�READ_PHONE_STATE
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
	 * ��ȡSIM����״̬ SIM_STATE_UNKNOWN δ֪״̬ 0 SIM_STATE_ABSENT û�忨 1
	 * SIM_STATE_PIN_REQUIRED ����״̬����Ҫ�û���PIN����� 2 SIM_STATE_PUK_REQUIRED
	 * ����״̬����Ҫ�û���PUK����� 3 SIM_STATE_NETWORK_LOCKED ����״̬����Ҫ�����PIN����� 4
	 * SIM_STATE_READY ����״̬ 5
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
	 * Ψһ���û�ID�� ���磺IMSI(�����ƶ��û�ʶ����) for a GSM phone. ��ҪȨ�ޣ�READ_PHONE_STATE
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
	 * ȡ�ú������ʼ���صı�ǩ����Ϊʶ��� ��ҪȨ�ޣ�READ_PHONE_STATE
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
	 * ��ȡ�����ʼ����룺 ��ҪȨ�ޣ�READ_PHONE_STATE
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
	 * ICC���Ƿ����
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
	 * �Ƿ�����: (��GSM��;��)
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
