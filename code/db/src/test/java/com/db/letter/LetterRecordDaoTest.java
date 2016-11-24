package com.db.letter;




import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;

import com.pojo.letter.LetterEntry;
import com.pojo.letter.LetterRecordEntry;
import com.pojo.letter.LetterRecordEntry.LetterRecordState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;


public class LetterRecordDaoTest {

	private LetterRecordDao dao =new LetterRecordDao();
	
	
	
	private LetterDao letterDao =new LetterDao();
	private LetterRecordDao letterRecordDao =new LetterRecordDao();
	
	
	private static Object lock =new Object();
	
	
	/**
	 * 
	 */
	@Test
	public void addLetterRecordEntry()
	{
		LetterRecordEntry e =new LetterRecordEntry(new ObjectId(),new ObjectId(),new ObjectId());
		dao.addLetterRecordEntry(e);
	}
	

	
	
	
	
	
	@Test
	public void readLetter()
	{
		//dao.readLetter(new ObjectId("553f3e7163e7ba306d22333d"), new ObjectId(), "us");;
	}
	
	@Test
	public void removeLetter()
	{
		//dao.removeLetter(new ObjectId("553f3e7163e7ba306d22333d"), new ObjectId(), null, "us");
	}
	
	
	
	@Test
	public void getList()
	{
		List<LetterRecordEntry> list=dao.getList(new ObjectId("5538a40063e765ffeb995348"), 0, 10);
		System.out.println(list);
	}
	
	
	
	
	
	
	@Test
	public void isExists()
	{
		//boolean is=dao.isExists(new ObjectId("5538969363e7049e1c51f57e"), new ObjectId("5538969363e7049e1c51f57c"));
		//System.out.println(is);
	}
	
	
	@Test
	public void sendLetter()
	{
		ObjectId myId =new ObjectId();
		
		List<ObjectId> receiveList =new ArrayList<ObjectId>();
		receiveList.add(new ObjectId());
		receiveList.add(new ObjectId());
		receiveList.add(new ObjectId());
		
		System.out.println("我自己的ID："+myId);
		System.out.println("信件好友ID："+receiveList);
		
		LetterEntry e =new LetterEntry(myId,"ceshi",receiveList);
		letterDao.addLetterEntry(e);
		handleLetterRecordForSend(myId,receiveList,e.getID());
		
	}
	
	
	@Test
	public void readLettertest()
	{
		try {
			handleLetterRecordForRead(new ObjectId("554193f763e77a390dc59e82"),new ObjectId("554193f763e77a390dc59e80"));
		} catch (IllegalParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void remove()
	{
		try
		{
		 handleLetterRecordForRemove(new ObjectId("5541956463e75ea57a9dbd91"),new ObjectId("5541956463e75ea57a9dbd8d"));
		}catch(Exception ex)
		{
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 
    /**
     * 看信更新信件记录
     * @param letterId
     * @param myId
     * @throws IllegalParamException
     */
    public void handleLetterRecordForRead(final ObjectId letterId,final ObjectId myId) throws IllegalParamException
    {
    
    	final LetterEntry le=letterDao.getLetterEntry(letterId);
    	if(null==le)
    		throw new IllegalParamException();
    	
    	if(!le.getSenderId().equals(myId)) //我是收信人
    	{

        	LetterRecordEntry letterRecordEntry= letterRecordDao.getLetterRecordEntry(le.getSenderId(), myId);
        	String field="us";
         	if(myId.equals(letterRecordEntry.getLetterUserId()))
         	field="lus";
        	letterRecordDao.readLetter(letterRecordEntry.getID(),  field);
    	}
    	
    }
    
    
 
    /**
     * 删除一个信件更新信件记录
     * @param letterId
     * @param myId
     * @throws IllegalParamException
     */
    public void handleLetterRecordForRemove(final ObjectId letterId,final ObjectId myId) throws IllegalParamException
    {
    	final LetterEntry le=letterDao.getLetterEntry(letterId);
    	if(null==le)
    		throw new IllegalParamException();
    	

					
			    	ObjectId sender=le.getSenderId();
			    	if(myId.equals(sender)) //我是发信者
			    	{
			    		 List<ObjectId> receiveIds= MongoUtils.getFieldObjectIDs(le.getReceiveList(), "ri");
			    		 for(ObjectId reveiveId:receiveIds)
			    		 {
			    			 LetterRecordEntry letterRecordEntry= letterRecordDao.getLetterRecordEntry(myId, reveiveId);
			    			 if(null==letterRecordEntry)
			    			 {
			    				
			    				 continue;
			    			 }
			    			 handleRemove(letterRecordEntry,myId,reveiveId,letterId);
			    		 }
			    	}
			    	else //我是收信者
			    	{
			    		 LetterRecordEntry letterRecordEntry= letterRecordDao.getLetterRecordEntry(sender, myId);
			    		 handleRemove(letterRecordEntry,myId,sender,letterId);
			    	}
			
    }
    
    /**
     * 更新信件记录
     * @param recerdId 信件记录ID
     * @param myId 我的ID
     * @param letterFriendId
     * @param letterId
     */
    public void handleRemove(  LetterRecordEntry record, ObjectId myId,ObjectId letterFriendId,ObjectId letterId)
    {
    	String field="us";
    	if(myId.equals(record.getLetterUserId()))
    		field="lus";
    	LetterRecordState state =record.getState(field);

    	boolean isUpdate=state.getId().equals(letterId);
    	ObjectId recentId=null;
    	if(isUpdate) //需要更新最新信件
    	{
    		List<LetterEntry> ls=letterDao.getLetterEntryListByPeerUserId(myId, letterFriendId, Constant.ZERO, Constant.TWO);
    		if(ls.size()==2)
    		{
    			recentId=ls.get(1).getID();
    		}
    	}
    	letterRecordDao.removeLetter(record.getID(), letterId, recentId, field, isUpdate);
    }
    
  
    /**
     * 更新信件记录,发信 时调用；
     * @param ui
     * @param friends
     * @param letterId
     */
    public void handleLetterRecordForSend(final ObjectId myId,final Collection<ObjectId> friends,final ObjectId letterId)
    {
    	 if(null!=friends && friends.size()>0)
	        {
	            for(ObjectId fi:friends)
	            {
	                handleLetterRecord(myId,fi,letterId);
	                try {
	                    Thread.sleep(5);
	                } catch (InterruptedException e) {
	                }
	            }
	        }
    }
    
    
    public  void handleLetterRecord(ObjectId myId,ObjectId letterFriendui,ObjectId letterId)
    {
    	
    	try
		{
    		LetterRecordEntry  e=letterRecordDao.getLetterRecordEntry(myId, letterFriendui);
        	if(null==e)
        	{
        		e=new LetterRecordEntry(myId, letterFriendui,letterId);
        		letterRecordDao.addLetterRecordEntry(e);
        	}
        	else
        	{
        		String field ="lus";
        		if(e.getLetterUserId().equals(myId))
        		{
        			field ="us";
        		}
        		letterRecordDao.update(e.getID(), letterId, field);
        	}
		}catch(Exception ex)
		{
			
		}
		
    }
	
	
	
}
