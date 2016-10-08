package com.fulaan.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.fulaan.dao.StaffDao;
import com.fulaan.entity.Staff;

@Service
public class StaffServiceImpl implements StaffService {

	@Resource
	StaffDao staffDao;

	@Override
	public Long getTotalNum(Class<Staff> entity) {
		return staffDao.countAll(entity);
	}

	@Override
	public List<Staff> getStaffsByPageNum(int pageNum, int size) {
		
		// 根据职工号排序
		String sql = "from Staff order by jobNumber";
		int index = (pageNum == 0 ? 0 : pageNum - 1) * size;
		List<Staff> staffs =  staffDao.getPageItem(sql, index, size);
		
		return staffs;
	}

	@Override
	public Staff get(Class<Staff> entity, Serializable id) {
		Staff staff = staffDao.get(entity, id);
		return staff;
	}

	@Override
	public void removeStaff(Staff staff) {
		staffDao.delete(staff);
	}

	@Override
	public List<Staff> findStaffByJobNum(String jobNum) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Staff.class);
		dc.add(Restrictions.eq("jobNumber", jobNum));
		List<Staff> staffs = staffDao.findByCriteria(dc);
		
		return staffs;
	}

	@Override
	public List<Staff> findStaffByLoginName(String loginName) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Staff.class);
		dc.add(Restrictions.eq("loginName", loginName));
		List<Staff> staffs = staffDao.findByCriteria(dc);
		
		return staffs;
	}

	@Override
	public void save(Staff staff) {
		staffDao.save(staff);
	}

	@Override
	public void update(Staff staff) {
		staffDao.update(staff);
	}

	@Override
	public List<Staff> findAllStaff() {
		return staffDao.findAll(Staff.class);
	}

	@Override
	public List<Staff> findByCriteria(DetachedCriteria criteria) {
		return staffDao.findByCriteria(criteria);
	}
	
}
