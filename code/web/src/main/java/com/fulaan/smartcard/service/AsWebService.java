package com.fulaan.smartcard.service;

import com.db.smartcard.AccountInfoDao;
import com.db.smartcard.DoorInfoDao;
import com.db.smartcard.KaoQinInfoDao;
import com.db.smartcard.TransInfoDao;
import com.pojo.smartcard.AccountInfoEntry;
import com.pojo.smartcard.DoorInfoEntry;
import com.pojo.smartcard.KaoQinInfoEntry;
import com.pojo.smartcard.TransInfoEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.axis2.AxisFault;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Created by guojing on 2016/5/27.
 */
public class AsWebService {

    private AsWebServiceJSONStub stub;

    private AccountInfoDao accountInfoDao=new AccountInfoDao();

    private KaoQinInfoDao kaoQinInfoDao=new KaoQinInfoDao();

    private KaoQinStateService kaoQinStateService=new KaoQinStateService();

    private DoorInfoDao doorInfoDao=new DoorInfoDao();

    private TransInfoDao transInfoDao=new TransInfoDao();


    /*private void demo(){
        try{
            ServiceClient sc = new ServiceClient();
            Options opts = new Options();
            String url = "http://122.195.71.210:8088/AsiaWeb/services/AsWebService.AsWebServiceHttpSoap11Endpoint?wsdl";
            EndpointReference end = new EndpointReference(url);
            opts.setTo(end);
            opts.setAction("accountFromCard");
            sc.setOptions(opts);

            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMNamespace omNs = fac.createOMNamespace("http://webService.com", "");
            OMElement method = fac.createOMElement("accountFromCardRequest",omNs);
            OMElement value = fac.createOMElement("cardNo",omNs);
            value.setText("10");
            method.addChild(value);
            OMElement res = sc.sendReceive(method);
            Map<String,Object> map= getResults(res);
            for(Map.Entry<String, Object> entry : map.entrySet()){
                System.out.println(entry.getKey()+":"+entry.getValue());
            }
            sc.cleanupTransport();
        }catch(AxisFault e){
            e.printStackTrace();
        }
    }

    public Map<String,Object> getResults(OMElement element) {
        if (element == null) {
            return null;
        }
        Iterator iterator = element.getChildElements();
        Iterator innerItr;
        Map<String,Object> map = new HashMap<String, Object>();
        OMElement result = null;
        while (iterator.hasNext()) {
            result = (OMElement) iterator.next();
            innerItr = result.getChildElements();
            while (innerItr.hasNext()) {  // 新增
                OMElement elem = (OMElement)innerItr.next();  // 新增
                map.put(elem.getLocalName(),elem.getText());
            }
        }
        return map;
    }*/

    private AsWebServiceJSONStub getAsWebServiceStub(){
        try {
            if (stub == null) {
                stub = new AsWebServiceJSONStub();
            }
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        return stub;
    }
    /**
     *  获取用户信息
     * @throws AxisFault
     */
    public void getAccount() {
        stub = getAsWebServiceStub();
        AsWebServiceJSONStub.GetAccount request = new AsWebServiceJSONStub.GetAccount();
        int pageSize=47;
        int page=1;
        request.setRequestStr("{'pageSize':"+pageSize+",'page':"+page+"}");

        AsWebServiceJSONStub.GetAccountResponse response;
        try {
            response = stub.getAccount(request);
            ObjectId schoolId=new ObjectId("55934c14f6f28b7261c19c62");
            System.out.println("getAccount： " + response.get_return());
            try {
                List<AccountInfoEntry> list=new ArrayList<AccountInfoEntry>();
                //将json字符串转换成json对象
                JSONObject dataJson = new JSONObject(response.get_return());
                //获取互动课堂Id
                int count=dataJson.getInt("count");
                //获取试题信息
                JSONArray datas = dataJson.getJSONArray("datas");
                //遍历试题信息
                for (int j = 0; j < datas.length(); j++) {
                    JSONObject account = datas.getJSONObject(j);

                    String name=account.getString("a_Name");
                    Long cardNo=account.getLong("c_Number");
                    String number=account.getString("a_Number");
                    Integer accounts=account.getInt("a_Accounts");
                    String physicalNo=account.getString("c_PhysicalNo");
                    String sex=account.getString("a_Sex");
                    String department=account.getString("d_Name");
                    String degree=account.getString("dg_Name");
                    Double money=account.getDouble("c_LastMoney");
                    Double moneyTwo=account.getDouble("c_LastMoney2");
                    Integer aflag=account.getInt("a_flag");
                    Integer cflag=account.getInt("c_Flag");

                    AccountInfoEntry info=new AccountInfoEntry(
                            schoolId,name,sex,cardNo,number,accounts,physicalNo,
                            degree,department,money,moneyTwo,aflag,cflag);
                    list.add(info);
                }
                accountInfoDao.addAccountInfoEntrys(list);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取用户考勤记录
     * @throws AxisFault
     */
    public void getUserKaoQin() {
        stub = getAsWebServiceStub();
        AsWebServiceJSONStub.GetKqData request = new AsWebServiceJSONStub.GetKqData();
        String beginDate="2015-03-24 00:00:00";
        String endDate="2016-06-30 00:00:00";
        request.setRequestStr("{'beginDate':'"+beginDate+"','endDate':'"+endDate+"'}");
        DateTimeUtils time=new DateTimeUtils();
        AsWebServiceJSONStub.GetKqDataResponse response;
        try {
            response = stub.getKqData(request);
            System.out.println("getKqData： " + response.get_return());
            try {
                List<KaoQinInfoEntry> list=new ArrayList<KaoQinInfoEntry>();
                //将json字符串转换成json对象
                JSONObject dataJson = new JSONObject(response.get_return());
                //获取互动课堂Id
                int count=dataJson.getInt("count");
                //获取试题信息
                JSONArray datas = dataJson.getJSONArray("datas");
                //遍历试题信息
                for (int j = 0; j < datas.length(); j++) {
                    JSONObject account = datas.getJSONObject(j);
                    String name=account.getString("a_Name");
                    Long cardNo=account.getLong("c_Number");
                    String number=account.getString("a_Number");
                    Integer accounts=account.getInt("a_Accounts");

                    String cardDateStr=account.getString("cardDate");//刷卡日期
                    Long cardDate=time.getStrToLongTime(cardDateStr);
                    Integer wId=account.getInt("w_ID");//工作站号
                    Integer posId=account.getInt("pos_ID");//终端机号
                    String inOutFlag=account.getString("inOutFlag");//进出标志 包含：混合，进，出

                    KaoQinInfoEntry info=new KaoQinInfoEntry(
                            name,cardNo,number,accounts,
                            cardDate,wId,posId,inOutFlag);
                    list.add(info);
                }
                kaoQinInfoDao.addKaoQinInfoEntrys(list);
                kaoQinStateService.updateKaoQinState(new ObjectId("55934c14f6f28b7261c19c62"));
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取门禁记录
     * @throws AxisFault
     */
    public void getDoorData() {
        stub = getAsWebServiceStub();
        AsWebServiceJSONStub.GetDoorData request = new AsWebServiceJSONStub.GetDoorData();
        String beginDate="2015-03-24 00:00:00";
        String endDate="2016-06-30 00:00:00";
        request.setRequestStr("{'beginDate':'"+beginDate+"','endDate':'"+endDate+"'}");

        AsWebServiceJSONStub.GetDoorDataResponse response;
        try {
            response = stub.getDoorData(request);
            System.out.println("getDoorData： " + response.get_return());
            try {
                List<DoorInfoEntry> list=new ArrayList<DoorInfoEntry>();
                //将json字符串转换成json对象
                JSONObject dataJson = new JSONObject(response.get_return());
                //获取互动课堂Id
                int count=dataJson.getInt("count");
                //获取试题信息
                JSONArray datas = dataJson.getJSONArray("datas");
                //遍历试题信息
                for (int j = 0; j < datas.length(); j++) {
                    JSONObject account = datas.getJSONObject(j);
                    Long cardNo=account.getLong("c_Number");
                    if(cardNo!=0l){
                        String name=account.getString("a_Name");
                        String number=account.getString("a_Number");
                        Integer accounts=account.getInt("a_Accounts");

                        String cardDateStr=account.getString("cardDate");//刷卡日期
                        Long cardDate=DateTimeUtils.getStrToLongTime(cardDateStr);

                        Integer wId=account.getInt("w_ID");//工作站号
                        Integer posId=account.getInt("pos_ID");//终端机号
                        String inOutFlag=account.getString("inOutFlag");//进出标志 包含：混合，进，出
                        String doorName=account.getString("doorName");
                        DoorInfoEntry info=new DoorInfoEntry(
                                name,cardNo,number,accounts,
                                cardDate,wId,posId,inOutFlag,doorName);
                        list.add(info);
                    }
                }
                doorInfoDao.addDoorInfoEntrys(list);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取交易记录
     * @throws AxisFault
     */
    public void getTransData() {
        stub = getAsWebServiceStub();
        AsWebServiceJSONStub.GetTransData request = new AsWebServiceJSONStub.GetTransData();
        String beginDate="2015-03-24 00:00:00";
        String endDate="2016-06-30 00:00:00";
        request.setRequestStr("{'beginDate':'"+beginDate+"','endDate':'"+endDate+"'}");

        AsWebServiceJSONStub.GetTransDataResponse response;
        try {
            response = stub.getTransData(request);
            System.out.println("getDoorData： " + response.get_return());
            try {
                List<TransInfoEntry> list=new ArrayList<TransInfoEntry>();
                //将json字符串转换成json对象
                JSONObject dataJson = new JSONObject(response.get_return());
                //获取互动课堂Id
                int count=dataJson.getInt("count");
                //获取试题信息
                JSONArray datas = dataJson.getJSONArray("datas");
                //遍历试题信息

                for (int j = 0; j < datas.length(); j++) {
                    JSONObject account = datas.getJSONObject(j);
                    Long cardNo=account.getLong("c_Number");
                    String name=account.getString("a_Name");
                    String number=account.getString("a_Number");
                    Integer accounts=account.getInt("a_Accounts");
                    String transType=account.getString("transType");//交易类型
                    Double transMoney=account.getDouble("t_Money");//交易金额
                    String transDateStr=account.getString("t_Date");//交易日期
                    Long transDate=DateTimeUtils.getStrToLongTime(transDateStr);
                    String accountDayStr=account.getString("accountDay");//记账日期
                    Long accountDay=DateTimeUtils.getStrToLongTime(accountDayStr);
                    Integer wId=account.getInt("w_ID");//工作站号
                    Integer posId=account.getInt("pos_ID");//终端机号
                    TransInfoEntry info=new TransInfoEntry(
                            name,cardNo,number,accounts,transType,
                            transMoney,transDate,accountDay,wId,posId);
                    list.add(info);
                }
                transInfoDao.addTransInfoEntrys(list);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public Map<Integer,ObjectId> getAllAccountIdMap(){
        return accountInfoDao.getAllAccountIdMap();
    }

    // 测试
    public static void main(String[] args) {
        AsWebService test = new AsWebService();
        //test.getAccount();
        //test.getUserKaoQin();
        //test.getDoorData();
        //test.getTransData();
        DateTimeUtils time=new DateTimeUtils();
        Date prevDay=time.getPrevDay(new Date(),-1);
        String prevDayStr = time.getDateToStrTime(prevDay);
        String currDayStr = time.getCurrDate();
        test.createUserKaoQinData(prevDayStr,currDayStr);
        test.createUserDoorData(prevDayStr,currDayStr);
    }

    /**
     *  获取用户考勤记录
     * @throws AxisFault
     */
    public void createUserKaoQinData(String startDate,String endDate) {
        DateTimeUtils time=new DateTimeUtils();
        List<String> dateAreas=time.getUseTimeArea(startDate,endDate);

        List<KaoQinInfoEntry> list=new ArrayList<KaoQinInfoEntry>();
        String[] timeArr1=new String[]{" 07:44:52"," 08:05:41"," 07:46:23"," 07:56:15"," 07:53:11"," 07:55:51",
                " 07:41:35"," 08:02:54"," 07:43:55"," 07:54:46"," 07:55:28"," 07:56:57"};
        String[] timeArr2=new String[]{" 17:06:12"," 17:04:34"," 17:16:42"," 17:14:32"," 17:11:35"," 16:31:33",
                " 17:06:12"," 17:04:34"," 17:16:42"," 17:14:32"," 17:11:35"," 16:31:33"};
        Random random = new Random();
        ObjectId schoolId=new ObjectId("55934c14f6f28b7261c19c62");
        List<AccountInfoEntry> accList = accountInfoDao.getAccountInfoEntryBySchoolId(schoolId);
        for(AccountInfoEntry acc:accList) {
            //遍历试题信息
            for (int j = 0; j < dateAreas.size(); j++) {
                int week = time.somedayIsWeekDay(time.stringToDate(dateAreas.get(j), time.DATE_YYYY_MM_DD));
                if (week == 0 || week == 6) {

                } else {
                    int index = random.nextInt(14);
                    if(index<12) {
                        String name = acc.getName();
                        Long cardNo = acc.getCardNo();
                        String number = acc.getNumber();
                        Integer accounts = acc.getAccounts();
                        Long cardDate1 = time.getStrToLongTime(dateAreas.get(j) + timeArr1[index]);
                        Long cardDate2 = time.getStrToLongTime(dateAreas.get(j) + timeArr2[index]);
                        Integer wId = 1;//工作站号
                        Integer posId = 1;//终端机号
                        String inOutFlag = "混合";//进出标志 包含：混合，进，出

                        KaoQinInfoEntry info1 = new KaoQinInfoEntry(
                                name, cardNo, number, accounts,
                                cardDate1, wId, posId, inOutFlag);
                        list.add(info1);
                        KaoQinInfoEntry info2 = new KaoQinInfoEntry(
                                name, cardNo, number, accounts,
                                cardDate2, wId, posId, inOutFlag);
                        list.add(info2);
                    }
                }
            }
        }

        kaoQinInfoDao.addKaoQinInfoEntrys(list);
        kaoQinStateService.updateKaoQinState(schoolId);
    }

    /**
     *  获取用户考勤记录
     * @throws AxisFault
     */
    public void createUserDoorData(String startDate,String endDate) {
        DateTimeUtils time=new DateTimeUtils();
        List<String> dateAreas=time.getUseTimeArea(startDate,endDate);

        List<DoorInfoEntry> list=new ArrayList<DoorInfoEntry>();
        String[] timeArr1=new String[]{" 07:44:51"," 08:05:51"," 07:46:51"," 07:56:51"," 07:55:51"," 07:55:51"};
        String[] timeArr2=new String[]{" 17:06:32"," 17:04:32"," 17:16:32"," 17:11:32"," 17:11:32"," 16:31:32"};
        String[] doorArr2=new String[]{"1号门","2号门"};
        Random random = new Random();
        ObjectId schoolId=new ObjectId("55934c14f6f28b7261c19c62");
        List<AccountInfoEntry> accList = accountInfoDao.getAccountInfoEntryBySchoolId(schoolId);
        for(AccountInfoEntry acc:accList) {
            if(acc.getAccounts()<50) {
                //遍历试题信息
                for (int j = 0; j < dateAreas.size(); j++) {
                    int week = time.somedayIsWeekDay(time.stringToDate(dateAreas.get(j), time.DATE_YYYY_MM_DD));
                    if (week == 0 || week == 6) {

                    } else {

                        int index = random.nextInt(6);
                        String name = acc.getName();
                        Long cardNo = acc.getCardNo();
                        String number = acc.getNumber();
                        Integer accounts = acc.getAccounts();
                        Long cardDate1 = time.getStrToLongTime(dateAreas.get(j) + timeArr1[index]);
                        Long cardDate2 = time.getStrToLongTime(dateAreas.get(j) + timeArr2[index]);
                        Integer wId = 2;//工作站号
                        Integer posId = 1;//终端机号
                        int index1 = random.nextInt(2);

                        DoorInfoEntry info1 = new DoorInfoEntry(
                                name, cardNo, number, accounts,
                                cardDate1, wId, posId, "进门", doorArr2[index1]);
                        list.add(info1);
                        DoorInfoEntry info2 = new DoorInfoEntry(
                                name, cardNo, number, accounts,
                                cardDate2, wId, posId, "出门",doorArr2[index1]);
                        list.add(info2);
                    }
                }
            }
        }
        doorInfoDao.addDoorInfoEntrys(list);
    }


}
