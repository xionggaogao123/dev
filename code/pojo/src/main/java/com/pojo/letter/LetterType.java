package com.pojo.letter;

/**
 * 信件类型
 * @author fourer
 *
 */
public enum LetterType {

	COMMON_LETTER(1,"普通信件"),
	ACTIVITY_INVITE(2,"活动邀请"),
    ACTIVITY_CANCEL(3,"活动取消")
	;
	
	private int type;
	private String name;
	
	
	private LetterType(int type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


    public static LetterType getLetterType(int type)
    {
        for(LetterType letterType:LetterType.values())
        {
            if(letterType.getType()==type)
            {
                return letterType;
            }
        }
        return null;
    }
}
