/**   
 * @Title: Page.java 
 * @Package com.pojo.app 
 * @Description: TODO(用一句话描述该文件是做什么的) 
 * @author yang.ling   
 * @date 2015年7月19日 下午11:04:38 
 * @version V1.0   
 * @Copyright ycode Co.,Ltd.
 */
package com.pojo.app;

/**
 * @ClassName: Page
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yang.ling
 * @date 2015年7月19日 下午11:04:38
 * 
 */
public class Page {

	private Integer currentPage = 1;
	private Integer pageSize = 10;
	public static Integer DEFAULT_PAGE_SIZE = 10;

	/**
	 * 默认构造函数, 默认当前页为第1页, 每页大小为10
	 */
	public Page() {

	}

	/**
	 * SimplePage构造函数
	 * 
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页大小
	 */
	public Page(Integer currentPage, Integer pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
