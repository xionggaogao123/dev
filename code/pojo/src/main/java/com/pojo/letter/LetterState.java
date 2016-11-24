package com.pojo.letter;

/**
 * 信件状态
 * @author fourer
 *
 */
public enum LetterState {
	LETTER_SEDND_FAILD(0,"发送失败"),
	LETTER_SEDND_SUCCESS(1,"发送成功"),
	LETTER_READED(2,"已经阅读"),
	LETTER_REPLY(3,"已经回复"),
	LETTER_DELETED(4,"已经删除"),
	;
	
	
	private int state;
	private String des;
	
	
	private LetterState(int state, String des) {
		this.state = state;
		this.des = des;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	
	
	public static LetterState getLetterState(int state)
	{
		for(LetterState letterState:LetterState.values())
		{
			if(letterState.getState()==state)
			{
				return letterState;
			}
		}
		return null;
	}
	
}
