package sql.dao;

import com.pojo.emarket.UserBalanceDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import sql.dataPojo.UserBalanceInfo;

import java.util.List;

public interface UserBalanceMapper {

	/**
	 * 查询当前用户余额信息
	 * @param userId
	 * @return
	 */
	public UserBalanceDTO selUserBalanceByUserId(String userId);
	
	/**
	 * 将当前用户余额信息insert到mysql db中
	 */
	public void insertUserBalance(UserBalanceDTO userBalanceInfo);
	
	/**
	 * 更新当前用户余额信息
	 */
	public void modifyUserBalance(UserBalanceDTO userBalanceInfo);

	/**
	 * 查询当前用户余额信息
	 * @param userBalanceInfo
	 * @return
	 */
	public List<UserBalanceDTO> selUserBalanceByUserIds(UserBalanceDTO userBalanceInfo);
	
}
