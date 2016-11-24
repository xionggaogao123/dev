package sql.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.emarket.UserBalanceDTO;
import com.pojo.user.UserEntry;
import org.bson.types.ObjectId;
import sql.dao.UserBalanceMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import sql.dataPojo.UserBalanceInfo;

public class UserBalanceDao {


//	private UserBalanceMapper userBalanceMapper;

	private SqlSessionFactory getSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        String resource = "configuration.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }
//
//	public UserBalanceDao() {
//		if(null == userBalanceMapper){
//			SqlSession sqlSession = getSessionFactory().openSession();
//			userBalanceMapper = sqlSession.getMapper(UserBalanceMapper.class);
//		}
//	}
	
	/**
	 * 充值
	 * @param userId
	 * @param money
	 */
	public void addMoneyToBalance(String userId, Double money){
		SqlSession sqlSession = getSessionFactory().openSession();
		UserBalanceMapper userBalanceMapper = sqlSession.getMapper(UserBalanceMapper.class);
		UserBalanceDTO ubInfo = userBalanceMapper.selUserBalanceByUserId(userId);
		if(null != ubInfo){
			UserBalanceDTO userBalanceInfo = new UserBalanceDTO();
			userBalanceInfo.setBalance(ubInfo.getBalance()+money);
			userBalanceInfo.setUserId(userId);
			userBalanceMapper.modifyUserBalance(userBalanceInfo);
		}
		sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 扣款
	 * @param userId
	 * @param money
	 */
	public void subMoneyFromBalance(String userId, Double money){
		SqlSession sqlSession = getSessionFactory().openSession();
		UserBalanceMapper userBalanceMapper = sqlSession.getMapper(UserBalanceMapper.class);
		UserBalanceDTO ubInfo = userBalanceMapper.selUserBalanceByUserId(userId);
		if (null != ubInfo) {
			UserBalanceDTO userBalanceInfo = new UserBalanceDTO();
			userBalanceInfo.setBalance(money);
			userBalanceInfo.setUserId(userId);
			userBalanceMapper.modifyUserBalance(userBalanceInfo);
		} else {
			UserBalanceDTO userBalanceInfo = new UserBalanceDTO();
			userBalanceInfo.setBalance(money);
			userBalanceInfo.setUserId(userId);
			userBalanceMapper.insertUserBalance(userBalanceInfo);
		}
			sqlSession.commit();
		sqlSession.close();
	}
	
	/**
	 * 查询用户账户余额信息
	 * @param userId
	 * @return
	 */
	public UserBalanceDTO getUserBalanceInfo(String userId){
		SqlSession sqlSession = getSessionFactory().openSession();
		UserBalanceMapper userBalanceMapper = sqlSession.getMapper(UserBalanceMapper.class);
		UserBalanceDTO ubInfo = userBalanceMapper.selUserBalanceByUserId(userId);
		if (ubInfo == null) {
			UserBalanceDTO userBalanceInfo = new UserBalanceDTO();
			userBalanceInfo.setBalance(0.0);
			userBalanceInfo.setUserId(userId);
			userBalanceMapper.insertUserBalance(userBalanceInfo);
			ubInfo = userBalanceMapper.selUserBalanceByUserId(userId);
		}
		sqlSession.commit();
		sqlSession.close();
		return ubInfo;
	}

	/**
	 * 查询用户账户余额信息
	 * @param dto
	 * @return
	 */
	public Map<String, UserBalanceDTO> getUserBalanceInfos(UserBalanceDTO dto) {
		SqlSession sqlSession = getSessionFactory().openSession();
		UserBalanceMapper userBalanceMapper = sqlSession.getMapper(UserBalanceMapper.class);
		List<UserBalanceDTO> ubInfos = userBalanceMapper.selUserBalanceByUserIds(dto);
		Map<String, UserBalanceDTO> userBalanceDTOMap = new HashMap<String, UserBalanceDTO>();
		if (ubInfos != null && ubInfos.size()!=0) {
			for (UserBalanceDTO userBalanceDTO : ubInfos) {
				userBalanceDTOMap.put(userBalanceDTO.getUserId(),userBalanceDTO);
			}
		}
		sqlSession.commit();
		sqlSession.close();
		return userBalanceDTOMap;
	}

	/**
	 * 添加用户信息
	 * @param userBalanceInfo
	 */
	public void addUserBalanceInfo(UserBalanceDTO userBalanceInfo) {
		SqlSession sqlSession = getSessionFactory().openSession();
		UserBalanceMapper userBalanceMapper = sqlSession.getMapper(UserBalanceMapper.class);
		userBalanceMapper.insertUserBalance(userBalanceInfo);
		sqlSession.commit();
		sqlSession.close();
	}
}
